package com.joshgm3z.ping.ui.screens.chat

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    viewModel: ChatViewModel = hiltViewModel(),
    onUserInfoClick: (String) -> Unit = {},
    onImageClick: (String) -> Unit = {},
    scrollToChatId: String = "",
) {
    var imagePreview by remember { mutableStateOf(Uri.parse("")) }
    when {
        imagePreview.path?.isNotEmpty() == true -> ImagePreview(
            imageUri = imagePreview,
            onBackClick = { imagePreview = Uri.parse("") },
            onSendClick = {
                viewModel.uploadChatImage(imagePreview, it)
                imagePreview = Uri.parse("")
            }
        )

        else -> {
            with(viewModel.uiState.collectAsState().value) {
                ChatScreen(
                    chatListState = chatListState,
                    scrollToChatId = scrollToChatId,
                    user = you ?: randomUser(),
                    onSendClick = { viewModel.onSendButtonClick(it) },
                    onUserInfoClick = { onUserInfoClick(you?.docId ?: "") },
                    onBackClick = {
                        viewModel.onScreenExit()
                        goHome()
                    },
                    openPreview = { imagePreview = it },
                    onImageClick = onImageClick,
                )
            }
        }
    }
}

@Composable
fun ChatScreen(
    chatListState: ChatListState,
    user: User = randomUser(),
    onSendClick: (String) -> Unit = {},
    onUserInfoClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    openPreview: (Uri) -> Unit = { },
    onImageClick: (String) -> Unit = { },
    scrollToChatId: String = "",
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
                        chats = chatListState.chats,
                        onImageClick = onImageClick,
                        scrollToChatId = scrollToChatId,
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
        InputBox2(
            onSendClick = { onSendClick(it) },
            openPreview = {
                openPreview(it)
            }
        )
    }
}

