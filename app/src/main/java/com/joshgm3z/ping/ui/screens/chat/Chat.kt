package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.data.util.getChatList
import com.joshgm3z.data.model.Chat
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.utils.getPrettyTime
import kotlinx.coroutines.launch

val chatBubbleRadius = 10.dp

@DarkPreview
@Composable
private fun PreviewScrollButton() {
    PingTheme {
        ScrollButton()
    }
}

@Composable
fun ChatList(
    chats: List<Chat> = emptyList(),
    onImageClick: (String) -> Unit = {},
    scrollToChatId: String = "",
) {
    Box(contentAlignment = Alignment.BottomCenter) {
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            reverseLayout = true,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(items = chats) {
                ChatItem(
                    chat = it,
                    onImageClick = {
                        onImageClick(it.docId)
                    }
                )
            }
        }
        listState.ScrollIfNeeded(scrollToChatId, chats)
        val coroutineScope = rememberCoroutineScope()
        ScrollButton(visible = listState.firstVisibleItemIndex > 5) {
            coroutineScope.launch {
                listState.scrollToItem(0)
            }
        }
    }
}

@Composable
fun ScrollButton(visible: Boolean = true, onClick: () -> Unit = {}) {
    AnimatedVisibility(visible) {
        Row(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = colorScheme.onPrimary)
                .clickable { onClick() }
                .padding(horizontal = 15.dp, vertical = 8.dp)
        ) {
            Icon(
                Icons.Default.ArrowDownward, contentDescription = null,
                tint = colorScheme.primary
            )
            Spacer(Modifier.size(10.dp))
            Text(
                text = "Scroll to bottom",
                color = colorScheme.primary
            )
        }
    }
}

@Composable
private fun LazyListState.ScrollIfNeeded(scrollToChatId: String, chats: List<Chat>) {
    if (scrollToChatId.isEmpty()) return
    val index = chats.indexOfFirst { it.docId == scrollToChatId }
    if (index != -1) {
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                animateScrollToItem(index)
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: Chat,
    onImageClick: () -> Unit = {}
) {
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
                .clip(RoundedCornerShape(chatBubbleRadius))
                .background(
                    when {
                        chat.isOutwards -> colorScheme.surfaceContainerHighest
                        else -> colorScheme.primary
                    }
                )
                .padding(2.dp)
                .widthIn(min = 5.dp, max = 250.dp),
            horizontalAlignment = when {
                chat.isOutwards -> Alignment.End
                else -> Alignment.Start
            }
        ) {
            InlineImagePreview(chat, onImageClick)
            ReplyPreview(chat)
            Message(chat)
        }
        Details(chat)
    }
}

@Composable
fun Message(chat: Chat) {
    if (chat.message.isNotEmpty()) {
        Text(
            text = chat.message,
            color = when {
                chat.isOutwards -> colorScheme.onSurface
                else -> colorScheme.onPrimary
            },
            modifier = Modifier.padding(horizontal = 5.dp)
        )
    }
}

@Composable
fun Details(chat: Chat) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (chat.isOutwards) StatusIcon(
            status = chat.status,
        )
        Text(
            text = getPrettyTime(chat.sentTime),
            color = Color.Gray,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
        )
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

//@Preview
@Composable
fun PreviewChatList() {
    LazyColumn(reverseLayout = true, modifier = Modifier.background(Color.Transparent)) {
        items(items = getChatList()) {
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
