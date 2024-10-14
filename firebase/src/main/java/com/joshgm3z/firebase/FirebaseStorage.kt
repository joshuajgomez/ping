package com.joshgm3z.firebase

import android.content.Context
import android.net.Uri
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.joshgm3z.utils.Logger
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorage
@Inject
constructor(
    context: Context
) {

    private val keyProfilePicture = "profile_pictures"

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
        onUploadComplete: (fileUrl: String) -> Unit,
        onUploadFailed: () -> Unit,
    ) {
        Logger.debug("fileName = [${fileName}], uri = [${uri}]")
        val profilePicReference = storageRef.child(keyProfilePicture)
        val picReference = profilePicReference.child(fileName)
        picReference.putFile(uri)
            .addOnSuccessListener {
                Logger.debug("upload success")
                onUploadComplete(picReference.path)
            }
            .addOnProgressListener {
                Logger.debug("progress: ${it.bytesTransferred} / ${it.totalByteCount}")
            }
            .addOnFailureListener {
                Logger.error("upload failed: ${it.message}")
                onUploadFailed()
            }
    }

}