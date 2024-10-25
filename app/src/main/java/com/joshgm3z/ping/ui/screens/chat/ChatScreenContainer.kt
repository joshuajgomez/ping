package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.data.util.getChatList
import com.joshgm3z.data.model.User
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.InfoCard
import com.joshgm3z.ping.ui.common.PingWallpaper
import com.joshgm3z.ping.ui.viewmodels.ChatListState
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel

@Preview
@Composable
private fun PreviewChatScreen() {
    PingTheme {
        ChatScreen(chatListState = ChatListState.Ready(getChatList()))
    }
}

@DarkPreview
@Composable
private fun PreviewChatScreenEmpty() {
    PingTheme {
        ChatScreen(chatListState = ChatListState.Empty)
    }
}

@DarkPreview
@Composable
private fun PreviewChatScreenLoading() {
    PingTheme {
        ChatScreen(chatListState = ChatListState.Loading)
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
    onImageClick: (String) -> Unit = {}
) {
    val uiState = chatViewModel.uiState.collectAsState()
    with(uiState.value) {
        ChatScreen(
            chatListState = chatListState,
            user = you ?: randomUser(),
            onSendClick = { chatViewModel.onSendButtonClick(it) },
            onUserInfoClick = { onUserInfoClick(you?.docId ?: "") },
            onBackClick = {
                chatViewModel.onScreenExit()
                goHome()
            },
            openPreview = {
                openPreview(
                    it,
                    me.docId,
                    you?.docId ?: ""
                )
            },
            onImageClick = onImageClick,
        )
    }
}

@Composable
fun ChatScreen(
    chatListState: ChatListState,
    user: User = randomUser(),
    onSendClick: (String) -> Unit = {},
    onUserInfoClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    openPreview: (String) -> Unit = { },
    onImageClick: (String) -> Unit = { },
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
            PingWallpaper {
                when (chatListState) {
                    is ChatListState.Ready -> ChatList(
                        chatListState.chats,
                        onImageClick
                    )

                    is ChatListState.Loading -> InfoCard(
                        "Just a second",
                        "Fetching messages",
                        Icons.Outlined.FileDownload
                    )

                    else -> InfoCard(
                        "Say hello",
                        "No messages yet",
                        Icons.Outlined.Forum
                    )
                }
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

