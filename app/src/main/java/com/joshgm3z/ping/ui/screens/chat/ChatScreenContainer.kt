package com.joshgm3z.ping.ui.screens.chat

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.data.model.Chat
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.data.util.getChatList
import com.joshgm3z.data.model.User
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.InfoCard
import com.joshgm3z.ping.ui.common.PingWallpaper
import com.joshgm3z.ping.ui.common.getCameraLauncher
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.viewmodels.ChatListState
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel
import com.joshgm3z.utils.FileUtil

@Preview
@Composable
private fun PreviewChatScreen() {
    PingTheme {
        ChatScreen(
            chatListState = ChatListState.Ready(getChatList()),
        )
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
    val cameraUri = getIfNotPreview { FileUtil.getUri(LocalContext.current) } ?: Uri.parse("")
    val cameraLauncher = getCameraLauncher {
        imagePreview = cameraUri
    }

    var inlinePreviewState by remember {
        mutableStateOf<InlinePreviewState>(InlinePreviewState.Empty)
    }

    when {
        imagePreview.path?.isNotEmpty() == true -> ImagePreview(
            imageUri = imagePreview,
            onBackClick = { imagePreview = Uri.parse("") },
            onSendClick = {
                inlinePreviewState = InlinePreviewState.Image(imagePreview)
                imagePreview = Uri.parse("")
            }
        )

        else -> {
            with(viewModel.uiState.collectAsState().value) {
                ChatScreen(
                    chatListState = chatListState,
                    scrollToChatId = scrollToChatId,
                    user = you ?: randomUser(),
                    onSendClick = {
                        viewModel.onSendButtonClick(it, inlinePreviewState)
                        inlinePreviewState = InlinePreviewState.Empty
                    },
                    onUserInfoClick = { onUserInfoClick(you?.docId ?: "") },
                    onBackClick = {
                        viewModel.onScreenExit()
                        goHome()
                    },
                    openCamera = { cameraLauncher.launch(cameraUri) },
                    onImageClick = onImageClick,
                    inlinePreviewState = inlinePreviewState,
                    deletePreview = { inlinePreviewState = InlinePreviewState.Empty },
                    onReplyClick = {
                        val fromName: String = when {
                            it.fromUserId == me.docId -> "You"
                            else -> you?.name ?: "Someone"
                        }
                        inlinePreviewState = InlinePreviewState.Reply(it, fromName)
                    }
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
    openCamera: () -> Unit = { },
    onImageClick: (String) -> Unit = { },
    scrollToChatId: String = "",
    inlinePreviewState: InlinePreviewState = InlinePreviewState.Empty,
    deletePreview: () -> Unit = {},
    onReplyClick: (Chat) -> Unit = {},
) {
    Scaffold(
        topBar = {
            ChatAppBar(
                user = user,
                onUserInfoClick = onUserInfoClick,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            InputBox(
                onSendClick = onSendClick,
                openCamera = openCamera,
                preview = inlinePreviewState,
                deletePreview = deletePreview,
            )
        }
    ) { paddingValues ->
        PingWallpaper {
            Box(
                modifier = Modifier.padding(paddingValues),
            ) {
                when (chatListState) {
                    is ChatListState.Ready -> ChatList(
                        chats = chatListState.chats,
                        onImageClick = onImageClick,
                        scrollToChatId = scrollToChatId,
                        onReplyClick = onReplyClick
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

    }
}


