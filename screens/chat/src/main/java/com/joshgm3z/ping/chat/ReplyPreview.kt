package com.joshgm3z.ping.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.data.model.Chat

@Composable
fun ReplyPreview(chat: Chat) {
    if (chat.replyToChatId.isNotEmpty()) {
        Column(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(chatBubbleRadius),
                    color = when {
                        chat.isOutwards -> colorScheme.surfaceContainerHigh
                        else -> colorScheme.primaryContainer.copy(alpha = 0.9f)
                    }
                )
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Text(
                text = "You",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    chat.isOutwards -> colorScheme.onBackground
                    else -> colorScheme.onPrimaryContainer
                }
            )
            Text(
                text = chat.replyToChatId,
                fontSize = 17.sp,
                color = when {
                    chat.isOutwards -> colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    else -> colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                }
            )
        }
    }
}