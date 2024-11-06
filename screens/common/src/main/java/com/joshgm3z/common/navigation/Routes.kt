package com.joshgm3z.common.navigation

import kotlinx.serialization.Serializable

@Serializable
data class PdfViewerRoute(
    val fileLocalUrl: String,
)