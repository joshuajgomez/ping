package com.joshgm3z.repository.impl

import android.net.Uri
import com.joshgm3z.firebase.FirebaseStorage
import com.joshgm3z.repository.api.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImageRepositoryProvider {
    @Binds
    abstract fun bindsImageRepository(
        imageRepositoryImpl: ImageRepositoryImpl
    ): ImageRepository
}

@Singleton
class ImageRepositoryImpl
@Inject
constructor(
    private val firebaseStorage: FirebaseStorage,
) : ImageRepository {
    override fun uploadImage(
        folderName: String,
        fileName: String,
        localUri: Uri,
        onSuccess: (String) -> Unit,
        onProgress: (Float) -> Unit,
        onError: (String) -> Unit,
    ) = firebaseStorage.uploadImage(
        folderName = folderName,
        fileName = fileName,
        uri = localUri,
        onUploadComplete = onSuccess,
        onUploadProgress = onProgress,
        onUploadFailed = onError,
    )

    override fun fetchProfileIconUrls(
        onFetch: (List<String>) -> Unit,
        onError: (String) -> Unit
    ) = firebaseStorage.fetchProfileIcons(onFetch, onError)

}