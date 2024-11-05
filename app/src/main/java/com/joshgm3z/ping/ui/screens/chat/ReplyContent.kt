package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.joshgm3z.data.model.Chat
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.SendingBar
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.ChatInlineUiState

@DarkPreview
@Composable
private fun PreviewReplyContentUploading(
    uiState: ChatInlineUiState = ChatInlineUiState.FileUpload(Chat("").apply {
        fileType = "pdf"
        fileName = "Soyo.pdf"
        fileSize = "30 MB"
        fileLocalUriToUpload = "llkk"
    }
    ),
) {
    PingTheme {
        Box(modifier = Modifier.width(250.dp)) {
            InlinePreviewContent(uiState)
        }
    }
}

@DarkPreview
@Composable
private fun PreviewReplyContentUploading2(
    uiState: ChatInlineUiState = ChatInlineUiState.FileUpload(Chat("").apply {
        fileType = "pdf"
        fileName = "Woyo.pdf yoyo yoyo.pdf yoyo.pdf yoyo.pdf yoyo.pdf yoyo.pdf.pdf"
        fileSize = "30 MB"
        fileLocalUriToUpload = "llkk"
    }
    ),
) {
    PingTheme {
        Box(modifier = Modifier.width(250.dp)) {
            InlinePreviewContent(uiState)
        }
    }
}

@DarkPreview
@Composable
private fun PreviewReplyContent(
    uiState: ChatInlineUiState = ChatInlineUiState.File(Chat("").apply {
        fileType = "pdf"
        fileName = "Moyo.pdf"
        fileSize = "30 MB"
        fileOnlineUrl = "llkk"
    }
    ),
) {
    PingTheme {
        PreviewReplyContentUploading(uiState)
    }
}

@DarkPreview
@Composable
private fun PreviewReplyContentLocalEmpty(
    uiState: ChatInlineUiState = ChatInlineUiState.File(Chat("").apply {
        fileType = "pdf"
        fileName = "Royo.pdf"
        fileSize = "30 MB"
        fileLocalUri = ""
    }
    ),
) {
    PingTheme {
        PreviewReplyContentUploading(uiState)
    }
}

@Composable
fun InlinePreviewContent(
    uiState: ChatInlineUiState,
) {
    val titleColor: Color = colorScheme.onBackground
    val textColor: Color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    when (uiState) {
        is ChatInlineUiState.Reply -> Column(Modifier.padding(8.dp)) {
            Text(
                text = uiState.fromUserName,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            Text(
                text = uiState.chat.message,
                fontSize = 17.sp,
                color = textColor
            )
        }

        is ChatInlineUiState.Image -> AsyncImage(
            model = uiState.chat.fileOnlineUrl,
            error = painterResource(R.drawable.wallpaper2),
            contentDescription = null,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )

        is ChatInlineUiState.File -> FilePreview(uiState.chat)
        is ChatInlineUiState.FileUpload -> FilePreview(uiState.chat)
        is ChatInlineUiState.ImageUpload -> Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = uiState.chat.fileLocalUriToUpload,
                error = painterResource(R.drawable.wallpaper2),
                contentDescription = null,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
            SendingBar(uiState.chat.imageUploadProgress)
        }

        is ChatInlineUiState.WebUrl -> {
            AsyncImage(
                model = null,
                contentDescription = null,
                error = painterResource(R.drawable.wallpaper2),
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
            Column(Modifier.padding(8.dp)) {
                Icon(
                    Icons.Default.Link,
                    contentDescription = null,
                    tint = colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(30.dp)
                )
                Text("Search Google", color = titleColor)
                Text("Search google for anything", color = textColor)
            }
        }

        else -> {}

    }
}

@Composable
private fun FilePreview(
    chat: Chat
) {
    Row(
        modifier = Modifier
            .background(colorScheme.surfaceContainerHigh, RoundedCornerShape(10.dp))
            .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()
    ) {
        Icon(
            when (chat.fileType) {
                "pdf" -> Icons.Default.PictureAsPdf
                "mp3" -> Icons.Default.AudioFile
                "mpeg" -> Icons.Default.VideoFile
                else -> Icons.Default.AttachFile
            },
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .background(
                    colorScheme.surface,
                    RoundedCornerShape(5.dp)
                )
                .padding(5.dp),
            tint = colorScheme.primary
        )
        Spacer(Modifier.size(7.dp))
        Column(Modifier.weight(1f)) {
            Text(
                chat.fileName,
                maxLines = 2,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
                color = colorScheme.onSurface
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    chat.fileSize,
                    fontSize = 12.sp,
                    color = colorScheme.onSurface.copy(alpha = .5f)
                )
                when {
                    chat.fileLocalUriToUpload.isNotEmpty() -> SendingBar(chat.imageUploadProgress)
                    chat.fileLocalUri.isEmpty() -> DownloadIcon()
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
