package com.joshgm3z.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FileIcon(fileType: String) {
    val modifier = Modifier
        .padding(top = 3.dp)
        .size(35.dp)
    Icon(
        imageVector = when (fileType) {
            "pdf" -> FilePdf
            "mp3" -> Icons.Default.AudioFile
            "mpeg" -> Icons.Default.VideoFile
            else -> Icons.Default.AttachFile
        },
        contentDescription = null,
        modifier = modifier,
        tint = colorScheme.primary,
    )
}