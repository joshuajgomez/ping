package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.User
import com.joshgm3z.ping.ui.common.LoadingContainer
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.utils.Logger
import com.joshgm3z.ping.utils.getChatList
import com.joshgm3z.ping.utils.randomUser
import com.joshgm3z.ping.ui.viewmodels.ChatUiState
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel

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

@Preview
@Composable
fun ChatScreen(
    chats: List<Chat> = getChatList(),
    user: User? = randomUser(),
    onSendClick: (message: String) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    PingTheme {
        Column {
            Title(user) { onBackClick() }
            ChatList(
                chats = chats,
                modifier = Modifier.weight(1f)
            )
            InputBox(onSendClick = { onSendClick(it) })
        }
    }
}
