package com.joshgm3z.ping.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.FilePreview
import com.joshgm3z.common.OnlineImage
import com.joshgm3z.common.SendingBar
import com.joshgm3z.common.defaultChatImage
import com.joshgm3z.common.defaultUserImage
import com.joshgm3z.data.model.Chat
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.ping.chat.viewmodels.ChatInlineUiState

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

        is ChatInlineUiState.Image -> OnlineImage(
            model = uiState.chat.fileOnlineUrl,
            error = defaultUserImage(),
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
        )

        is ChatInlineUiState.File -> FilePreview(uiState.chat)
        is ChatInlineUiState.FileUpload -> FilePreview(uiState.chat)
        is ChatInlineUiState.ImageUpload -> Box(contentAlignment = Alignment.BottomEnd) {
            OnlineImage(
                model = uiState.chat.fileLocalUriToUpload,
                error = defaultChatImage(),
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
            )
            SendingBar(uiState.chat.fileUploadProgress)
        }

        is ChatInlineUiState.WebUrl -> {
            OnlineImage(
                model = null,
                error = defaultChatImage(),
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
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
