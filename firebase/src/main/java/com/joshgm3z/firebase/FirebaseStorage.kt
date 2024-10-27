package com.joshgm3z.firebase

import android.content.Context
import android.net.Uri
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.joshgm3z.utils.Logger
import com.joshgm3z.utils.MainThreadScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorage
@Inject
constructor(
    context: Context,
    private val scope: CoroutineScope,
) {

    private val keyProfilePicture = "profile_pictures"
    private val keyProfileIcons = "profile_icons"

    private val storage = Firebase.storage

    private val storageRef = storage.reference

    init {
        FirebaseApp.initializeApp(context)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance())
    }

    fun uploadImage(
        fileName: String,
        uri: Uri,
        onUploadComplete: (String) -> Unit = {},
        onUploadProgress: (Float) -> Unit = {},
        onUploadFailed: (String) -> Unit = {},
    ) {
        Logger.debug("fileName = [${fileName}], uri = [${uri}]")
        val profilePicReference = storageRef.child(keyProfilePicture)
        val picReference = profilePicReference.child(fileName)
        picReference.putFile(uri)
            .addOnSuccessListener {
                Logger.debug("upload success")
                picReference.downloadUrl.addOnSuccessListener {
                    onUploadComplete(it.toString())
                }
            }
            .addOnProgressListener {
                val progress = (it.bytesTransferred.toFloat() / it.totalByteCount.toFloat()) * 100
                Logger.debug("progress: $progress% [${it.bytesTransferred}/${it.totalByteCount}]")
                onUploadProgress(progress)
            }
            .addOnFailureListener {
                Logger.error("upload failed: ${it.message}")
                onUploadFailed(it.message.toString())
            }
    }

    fun fetchProfileIcons(
        onFetched: (List<String>) -> Unit,
        onError: (String) -> Unit,
    ) {
        Logger.entry
        storageRef.child(keyProfileIcons)
            .listAll()
            .addOnSuccessListener { it ->
                Logger.debug("images = ${it.items.size}")
                val imageUrls = mutableListOf<String>()
                val latch = CountDownLatch(it.items.size)
                it.items.forEach {
                    it.downloadUrl.addOnSuccessListener { uri ->
                        imageUrls.add(uri.toString())
                        latch.countDown()
                    }
                }
                scope.launch {
                    latch.await(5, TimeUnit.SECONDS)
                    onFetched(imageUrls)
                }
            }
            .addOnFailureListener {
                Logger.error(it.message.toString())
                onError(it.message.toString())
            }
    }

}
