package com.joshgm3z.repository.api

import kotlinx.coroutines.flow.Flow

sealed class ImageUploadState {
    data class Complete(val url: String) : ImageUploadState()
    data class Progress(val progress: Float) : ImageUploadState()
    data class Error(val error: String) : ImageUploadState()
}

interface ImageRepository {

    fun uploadImage(
        fileName: String,
        localUrl: String,
    ): Flow<ImageUploadState>

    fun fetchProfileIconUrls(
        onFetch: (List<String>) -> Unit,
        onError: (String) -> Unit,
    )
}