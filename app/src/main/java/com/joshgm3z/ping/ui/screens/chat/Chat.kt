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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.ui.theme.Gray40
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Purple20
import com.joshgm3z.ping.ui.theme.Purple40
import com.joshgm3z.ping.utils.getChatList
import com.joshgm3z.ping.utils.getPrettyTime

@Composable
fun ChatList(
    chats: List<Chat> = emptyList(),
    modifier: Modifier = Modifier,
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
        val chat = Chat.random()
        chat.isOutwards = false
        ChatItem(chat)
    }
}

@Preview
@Composable
fun PreviewOutgoingChat() {
    PingTheme {
        val chat = Chat.random()
        chat.isOutwards = true
        ChatItem(chat)
    }
}

@Composable
fun ChatItem(chat: Chat = Chat.random()) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = if (chat.isOutwards) Alignment.End else Alignment.Start
    ) {
        Box(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (chat.isOutwards) Purple40 else Gray40)
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .widthIn(min = 5.dp, max = 250.dp)
        ) {
            Text(
                text = chat.message,
                fontSize = 18.sp,
                color = if (chat.isOutwards) Purple20 else Color.DarkGray
            )
        }
        Row {
            if (chat.isOutwards) StatusIcon(status = chat.status)
            Text(
                text = getPrettyTime(chat.sentTime),
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
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
        modifier = modifier.size(16.dp)
    )
}

@Preview
@Composable
fun PreviewChatList() {
    LazyColumn(reverseLayout = true, modifier = Modifier.background(Color.Transparent)) {
        items(items = getChatList()) {
            ChatItem(chat = it)
        }
    }
}
