package com.joshgm3z.repository.api

interface ImageRepository {

    fun uploadImage(
        fileName: String,
        localUrl: String,
        onSuccess: (String) -> Unit,
        onProgress: (Float) -> Unit,
        onError: (String) -> Unit,
    )

    fun fetchProfileIconUrls(
        onFetch: (List<String>) -> Unit,
        onError: (String) -> Unit,
    )
}