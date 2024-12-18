package com.joshgm3z.repository.api

import android.net.Uri

interface ImageRepository {

    fun uploadFile(
        folderName: String,
        fileName: String,
        localUri: Uri,
        onSuccess: (String) -> Unit = {},
        onProgress: (Float) -> Unit = {},
        onError: (String) -> Unit = {},
    )

    fun fetchProfileIconUrls(
        onFetch: (List<String>) -> Unit,
        onError: (String) -> Unit,
    )
}