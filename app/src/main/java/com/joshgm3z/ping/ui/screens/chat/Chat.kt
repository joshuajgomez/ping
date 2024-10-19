package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.theme.Gray30
import com.joshgm3z.ping.ui.theme.Gray40
import com.joshgm3z.ping.ui.theme.Gray50
import com.joshgm3z.ping.ui.theme.Gray60
import com.joshgm3z.ping.ui.theme.Gray70
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.data.util.getChatList
import com.joshgm3z.data.model.Chat
import com.joshgm3z.ping.utils.getPrettyTime

@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    chats: List<Chat> = emptyList(),
) {
    LazyColumn(reverseLayout = true, modifier = modifier) {
        items(items = chats) {
            ChatItem(chat = it)
        }
    }
}

@Preview
@Composable
fun PreviewIncomingChat() {
    PingTheme {
        val chat = Chat.random("I just wanted to conform one thing")
        chat.isOutwards = false
        ChatItem(chat)
    }
}

@Preview
@Composable
fun PreviewOutgoingChat() {
    PingTheme {
        val chat = Chat.random("I just wanted to conform one thing")
        chat.isOutwards = true
        ChatItem(chat)
    }
}

@Preview
@Composable
fun PreviewOutgoingChatReply() {
    PingTheme {
        val chat = Chat.random("me too")
        chat.isOutwards = true
        chat.replyToChatId = "im going to movie"
        ChatItem(chat)
    }
}

@Preview
@Composable
fun PreviewIncomingChatReply() {
    PingTheme {
        val chat = Chat.random("me too")
        chat.isOutwards = false
        chat.replyToChatId = "im going to movie"
        ChatItem(chat)
    }
}

@Preview
@Composable
fun PreviewIncomingChatImage() {
    PingTheme {
        val chat = Chat("")
        chat.imageUrl = "im going to movie"
        chat.isOutwards = false
        ChatItem(chat)
    }
}

@Composable
fun ChatItem(chat: Chat) {
    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = when {
            chat.isOutwards -> Alignment.End
            else -> Alignment.Start
        }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(9.dp))
                .background(
                    when {
                        chat.isOutwards -> colorScheme.surfaceContainerHighest
                        else -> colorScheme.primary
                    }
                )
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .widthIn(min = 5.dp, max = 250.dp),
            horizontalAlignment = when {
                chat.isOutwards -> Alignment.End
                else -> Alignment.Start
            }
        ) {
            ImagePreview(chat)
            ReplyPreview(chat)
            Message(chat)
        }
        Details(chat)
    }
}

@Composable
fun ImagePreview(chat: Chat) {
    if (chat.imageUrl.isEmpty()) return
    Box {
        ChatImage(
            imageUrl = chat.imageUrl,
            placeHolderColor = when {
                chat.isOutwards -> colorScheme.surfaceContainerHighest
                else -> colorScheme.primaryContainer.copy(alpha = 0.9f)
            }
        )
    }
}

@Composable
fun Message(chat: Chat) {
    if (chat.message.isNotEmpty()) {
        Text(
            text = chat.message,
            fontSize = 18.sp,
            color = when {
                chat.isOutwards -> colorScheme.onSurface
                else -> colorScheme.onPrimary
            },
            modifier = Modifier.padding(start = 2.dp, end = 2.dp, top = 5.dp)
        )
    }
}

@Composable
fun Details(chat: Chat) {
    Row {
        if (chat.isOutwards) StatusIcon(
            status = chat.status,
            modifier = Modifier.padding(top = 3.dp)
        )
        Text(
            text = getPrettyTime(chat.sentTime),
            color = Color.Gray,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
        )
    }
}

@Composable
fun ReplyPreview(chat: Chat) {
    if (chat.replyToChatId.isNotEmpty()) {
        Column(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(10.dp),
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

@Preview
@Composable
fun PreviewStatusIcon() {
    PingTheme {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.width(100.dp)
        ) {
            StatusIcon(status = Chat.SAVED)
            StatusIcon(status = Chat.SENT)
            StatusIcon(status = Chat.DELIVERED)
            StatusIcon(status = Chat.READ)
        }
    }
}

@Composable
fun StatusIcon(modifier: Modifier = Modifier, status: Long = Chat.READ) {
    Icon(
        imageVector = when (status) {
            Chat.SENT -> Icons.Default.Done
            Chat.DELIVERED -> Icons.Default.DoneAll
            Chat.READ -> Icons.Default.DoneAll
            else -> Icons.Default.AccessTime
        },
        contentDescription = "status",
        tint = when (status) {
            Chat.SAVED -> colorScheme.outlineVariant
            Chat.READ -> colorScheme.surfaceTint
            else -> colorScheme.onSurface
        },
        modifier = modifier.size(13.dp)
    )
}

//@Preview
@Composable
fun PreviewChatList() {
    LazyColumn(reverseLayout = true, modifier = Modifier.background(Color.Transparent)) {
        items(items = getChatList()) {
            ChatItem(chat = it)
        }
    }
}
