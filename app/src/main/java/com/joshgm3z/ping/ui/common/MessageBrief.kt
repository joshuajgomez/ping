package com.joshgm3z.ping.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.util.FileType
import com.joshgm3z.ping.ui.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewMessageBrief() {
    PingTheme {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            MessageBrief(Chat("Heyyo"))
            MessageBrief(Chat("Heyyo").apply {
                fileType = "pdf"
                fileName = "Some file.pdf"
            })
            MessageBrief(Chat("").apply {
                fileType = "pdf"
                fileName = "Some file.pdf"
            })
            MessageBrief(Chat("").apply {
                fileType = "pdf"
                fileName = ""
            })
            MessageBrief(Chat("").apply {
                fileType = "png"
                fileName = "chat_image.png"
            })
        }
    }
}

@Composable
fun MessageBrief(chat: Chat) {
    Row {
        if (chat.fileType.isNotEmpty()) {
            Box(modifier = Modifier.padding(top = 4.dp)) {
                MediaIcon(chat)
            }
        }
        Text(
            text = getMessageBrief(chat),
            color = colorScheme.onSurfaceVariant,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

fun getMessageBrief(chat: Chat): String = when {
    chat.message.isNotEmpty() -> chat.message
    chat.fileType() == FileType.Image -> "Photo"
    chat.fileName.isNotEmpty() -> chat.fileName
    else -> "File"
}

@Composable
fun MediaIcon(chat: Chat) {
    when (chat.fileType()) {
        FileType.Image -> Icons.Default.CameraAlt
        else -> Icons.Default.AttachFile
    }.let {
        Icon(
            it,
            contentDescription = null,
            tint = colorScheme.onSurface,
            modifier = Modifier
                .padding(end = 3.dp)
                .size(17.dp)
        )
    }
}