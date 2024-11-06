package com.joshgm3z.common

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.data.model.Chat
import com.joshgm3z.utils.FileUtil

@Composable
fun FilePreview(
    chat: Chat
) {
    FilePreviewContent(
        chat.fileType,
        chat.fileName,
        chat.fileSize,
        chat.fileLocalUriToUpload,
        chat.fileLocalUri,
        chat.fileUploadProgress
    )
}

@Composable
fun FilePreview(
    fileUri: Uri
) {
    when {
        LocalInspectionMode.current -> FilePreviewContent(
            "pdf",
            "This file.pdf",
            "23 MB",
            "",
            "",
            0f
        )

        else -> FileUtil(LocalContext.current).apply {
            FilePreviewContent(
                getFileTypeString(fileUri),
                getFileName(fileUri),
                getFileSizeString(fileUri),
                "",
                "",
                0f
            )
        }
    }
}

@Composable
private fun FilePreviewContent(
    fileType: String,
    fileName: String,
    fileSize: String,
    fileLocalUriToUpload: String,
    fileLocalUri: String,
    fileUploadProgress: Float,
) {
    Row(
        modifier = Modifier
            .background(colorScheme.surfaceContainerHigh, RoundedCornerShape(10.dp))
            .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()
    ) {
        FileIcon(fileType)
        Spacer(Modifier.size(7.dp))
        Column(Modifier.weight(1f)) {
            Text(
                fileName,
                maxLines = 2,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                color = colorScheme.onSurface,
                lineHeight = 22.sp
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    fileSize,
                    fontSize = 12.sp,
                    color = colorScheme.onSurface.copy(alpha = .5f),
                    lineHeight = 17.sp
                )
                when {
                    fileLocalUriToUpload.isNotEmpty() -> FileSendingBar(fileUploadProgress)
                    fileLocalUri.isEmpty() -> DownloadIcon()
                }
            }
        }
    }
}

@Composable
fun DownloadIcon() {
    Icon(
        Icons.Default.CloudDownload,
        contentDescription = null,
        tint = colorScheme.onSurface.copy(alpha = 0.5f),
        modifier = Modifier
            .padding(end = 5.dp)
            .size(20.dp)
    )
}

@Composable
fun FileSendingBar(progress: Float) {
    Row(
        Modifier
            .padding(top = 3.dp)
            .padding(horizontal = 5.dp, vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val color = colorScheme.onSurface.copy(alpha = 0.8f)
        when (progress) {
            0f -> {
                CircularProgressIndicator(
                    color = color,
                    modifier = Modifier.size(15.dp),

                    )
            }

            else -> CircularProgressIndicator(
                progress = { progress / 100 },
                color = color,
                modifier = Modifier.size(15.dp),
            )
        }
        Spacer(Modifier.size(5.dp))
        Text("Sending", color = color, fontSize = 13.sp)
    }
}

@Composable
fun SendingBar(progress: Float) {
    Row(
        Modifier
            .padding(top = 6.dp)
            .background(
                Color.Black.copy(alpha = 0.5f),
                RoundedCornerShape(5.dp)
            )
            .padding(horizontal = 5.dp, vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val color = colorScheme.onSurface.copy(alpha = 0.8f)
        when (progress) {
            0f -> {
                CircularProgressIndicator(
                    color = color,
                    modifier = Modifier.size(15.dp)
                )
            }

            else -> CircularProgressIndicator(
                progress = { progress / 100 },
                color = color,
                modifier = Modifier.size(15.dp)
            )
        }
        Spacer(Modifier.size(5.dp))
        Text("Sending", color = color, fontSize = 13.sp)
    }
}