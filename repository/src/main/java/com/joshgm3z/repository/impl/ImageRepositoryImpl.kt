package com.joshgm3z.repository.impl

import com.joshgm3z.firebase.FirebaseStorage
import com.joshgm3z.repository.api.ImageRepository
import com.joshgm3z.repository.api.ImageUploadState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
    private val scope: CoroutineScope,
) : ImageRepository {
    override fun uploadImage(
        fileName: String,
        localUrl: String,
    ): Flow<ImageUploadState> = flow {
        firebaseStorage.uploadImage(
            fileName = fileName,
            url = localUrl,
            onUploadComplete = {
                scope.launch {
                    emit(ImageUploadState.Complete(it))
                }
            },
            onUploadProgress = {
                scope.launch {
                    emit(ImageUploadState.Progress(it))
                }
            },
            onUploadFailed = {
                scope.launch {
                    emit(ImageUploadState.Error(it))
                }
            },
        )
    }

    override fun fetchProfileIconUrls(
        onFetch: (List<String>) -> Unit,
        onError: (String) -> Unit
    ) = firebaseStorage.fetchProfileIcons(onFetch, onError)

}