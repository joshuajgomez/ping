package com.joshgm3z.ping.ui.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.ui.common.LoadingContainer
import com.joshgm3z.ping.utils.randomUser
import com.joshgm3z.ping.viewmodels.ChatUiState
import com.joshgm3z.ping.viewmodels.ChatViewModel

@Composable
fun ChatScreenContainer(chatViewModel: ChatViewModel, onBackClick: () -> Unit) {
    val uiState = chatViewModel.uiState.collectAsState()
    when (uiState.value) {
        is ChatUiState.Loading -> LoadingContainer(message = (uiState.value as ChatUiState.Loading).message)
        is ChatUiState.Ready -> ChatScreen(
            chatListLive = chatViewModel.chatList,
            user = chatViewModel.user,
            onSendClick = { chatViewModel.onSendButtonClick(it) },
            onBackClick = { onBackClick() }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ChatScreen(
    chatListLive: LiveData<List<Chat>> = MutableLiveData(),
    user: User? = randomUser(),
    onSendClick: (message: String) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Title(user) { onBackClick() }
        Surface(modifier = Modifier.weight(1f)) {
            ChatList(chatListLive)
        }
        InputBox(onSendClick = { onSendClick(it) })
    }
}
