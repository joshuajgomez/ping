package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.LoadingContainer
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.data.util.getChatList
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.ping.R
import com.joshgm3z.ping.graph.ChatImagePreview
import com.joshgm3z.ping.graph.Home
import com.joshgm3z.ping.graph.UserInfo
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
fun ChatScreenContainer(
    goHome: () -> Unit = {},
    chatViewModel: ChatViewModel = hiltViewModel(),
    onUserInfoClick: (String) -> Unit = {},
    openPreview: (
        imageUrl: String,
        myUserId: String,
        toUserId: String,
    ) -> Unit = { _, _, _ -> },
) {
    val uiState = chatViewModel.uiState.collectAsState()
    when (uiState.value) {
        is ChatUiState.Loading -> {
            LoadingContainer(message = (uiState.value as ChatUiState.Loading).message)
        }

        is ChatUiState.Ready -> {
            val ready = uiState.value as ChatUiState.Ready
            val user: User = ready.you
            val chats: List<Chat> = ready.chats
            ChatScreen(
                chats = chats,
                user = user,
                onSendClick = { chatViewModel.onSendButtonClick(it) },
                onUserInfoClick = { onUserInfoClick(user.docId) },
                onBackClick = {
                    chatViewModel.onScreenExit()
                    goHome()
                },
                openPreview = {
                    openPreview(
                        it,
                        ready.me.docId,
                        ready.you.docId
                    )
                }
            )
        }
    }
}

@Composable
fun ChatScreen(
    chats: List<Chat> = getChatList(),
    user: User = randomUser(),
    onSendClick: (message: String) -> Unit = {},
    onUserInfoClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    openPreview: (imageUrl: String) -> Unit = { }
) {
    Column {
        ChatAppBar(
            user = user,
            onUserInfoClick = onUserInfoClick,
            onBackClick = onBackClick
        )
        Box(
            modifier = Modifier.weight(1f),
        ) {
            Image(
                painterResource(R.drawable.chat_wallpaper),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            when {
                chats.isNotEmpty() -> ChatList(chats)
                else -> EmptyChat()
            }
        }
        InputBox(
            onSendClick = { onSendClick(it) },
            openPreview = {
                openPreview(it)
            }
        )
    }
}

@Composable
fun EmptyChat(
    modifier: Modifier = Modifier,
    message: String = "No messages in this chat"
) {
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
            tint = colorScheme.primary
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
