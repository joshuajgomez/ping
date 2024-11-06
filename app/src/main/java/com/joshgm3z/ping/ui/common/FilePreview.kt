package com.joshgm3z.ping.ui.common

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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.data.model.Chat
import com.joshgm3z.ping.ui.screens.chat.DownloadIcon
import com.joshgm3z.ping.ui.screens.chat.FileIcon
import com.joshgm3z.ping.ui.screens.chat.FileSendingBar
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
