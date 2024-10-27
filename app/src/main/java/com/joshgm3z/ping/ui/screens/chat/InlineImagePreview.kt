package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.joshgm3z.data.model.Chat
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.theme.PingTheme

@Composable
fun InlineImagePreview(
    chat: Chat,
    onImageClick: () -> Unit,
) {
    if (chat.imageUrl.isEmpty() && chat.imageUploadUri.isEmpty()) return
    val uploadOngoing = chat.imageUploadUri.isNotEmpty()
    Box(
        Modifier.clickable(!uploadOngoing) { onImageClick() },
        contentAlignment = Alignment.Center
    ) {
        ChatImage(
            modifier = Modifier.fillMaxSize(),
            imageUrl = when {
                chat.imageUrl.isNotEmpty() -> chat.imageUrl
                else -> chat.imageUploadUri
            },
            placeHolderColor = when {
                chat.isOutwards -> colorScheme.surfaceContainerHighest
                else -> colorScheme.primaryContainer.copy(alpha = 0.9f)
            }
        )
        if (uploadOngoing) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    with(chat.imageUploadProgress / 100) {
                        when {
                            this == 0f -> CircularProgressIndicator(
                                color = colorScheme.primary,
                                modifier = Modifier.size(60.dp)
                            )

                            else -> CircularProgressIndicator(
                                progress = { this },
                                color = colorScheme.primary,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }
                    Icon(
                        Icons.Default.Upload,
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(Modifier.height(5.dp))
                Text(
                    "Uploading",
                    color = colorScheme.onSurface
                )
            }
        }
    }
}

@DarkPreview
@Composable
fun PreviewSendingChatImage() {
    PingTheme {
        val chat = Chat("")
        chat.imageUploadUri = "aa"
        ChatItem(chat)
    }
}