package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.User
import com.joshgm3z.ping.ui.common.LoadingContainer
import com.joshgm3z.ping.ui.screens.frx.PasswordInput
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.utils.getChatList
import com.joshgm3z.ping.utils.randomUser
import com.joshgm3z.ping.ui.viewmodels.ChatUiState
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel

@Preview
@Composable
private fun PreviewEmptyChat() {
    PingTheme {
        EmptyChat()
    }
}

@Preview
@Composable
private fun PreviewChatScreen() {
    PingTheme {
        ChatScreen()
    }
}

@Preview
@Composable
private fun PreviewChatScreenEmpty() {
    PingTheme {
        ChatScreen(chats = emptyList())
    }
}

@Preview
@Composable
private fun PreviewChatScreenLoading() {
    PingTheme {
        LoadingContainer()
    }
}

@Composable
fun ChatScreenContainer(chatViewModel: ChatViewModel, onBackClick: () -> Unit) {
    val uiState = chatViewModel.uiState.collectAsState()
    when (uiState.value) {
        is ChatUiState.Loading -> {
            LoadingContainer(message = (uiState.value as ChatUiState.Loading).message)
        }

        is ChatUiState.Ready -> {
            val user: User = (uiState.value as ChatUiState.Ready).you
            val chats: List<Chat> = (uiState.value as ChatUiState.Ready).chats
            ChatScreen(
                chats = chats,
                user = user,
                onSendClick = { chatViewModel.onSendButtonClick(it) },
                onBackClick = { onBackClick() }
            )
        }
    }
}

@Composable
fun ChatScreen(
    chats: List<Chat> = getChatList(),
    user: User = randomUser(),
    onSendClick: (message: String) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Column {
        ChatAppBar(user = user) { onBackClick() }
        if (chats.isNotEmpty()) {
            ChatList(
                modifier = Modifier.weight(1f),
                chats = chats,
            )
        } else {
            EmptyChat(modifier = Modifier.weight(1f))
        }
        InputBox(onSendClick = { onSendClick(it) })
    }
}

@Composable
fun EmptyChat(modifier: Modifier = Modifier, message: String = "No messages in this chat") {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 150.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Forum,
            contentDescription = "empty message",
            modifier = Modifier.size(80.dp),
            tint = colorScheme.outline
        )
        Text(
            text = "Say hello!",
            fontSize = 20.sp,
            color = colorScheme.outline,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = message,
            color = colorScheme.outline
        )
    }
}
