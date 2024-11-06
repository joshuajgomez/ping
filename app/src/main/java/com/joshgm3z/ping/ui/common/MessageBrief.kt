package com.joshgm3z.ping.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.data.model.Chat

@Composable
fun MessageBrief(chat: Chat) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (chat.fileType.isNotEmpty()) {
            MediaIcon(chat)
        }
        Text(
            text = getMessageBrief(chat),
            color = colorScheme.onSurfaceVariant,
            fontSize = 15.sp,
            modifier = Modifier.widthIn(max = 260.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

fun getMessageBrief(chat: Chat): String = when {
    chat.message.isNotEmpty() -> chat.message
    chat.fileType == "jpeg" || chat.fileType == "jpg" || chat.fileType == "png" -> "Photo"
    chat.fileName.isNotEmpty() -> chat.fileName
    else -> "File"
}

@Composable
fun MediaIcon(chat: Chat) {
    when (chat.fileType) {
        "jpeg", "jpg", "png" -> Icons.Default.CameraAlt
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