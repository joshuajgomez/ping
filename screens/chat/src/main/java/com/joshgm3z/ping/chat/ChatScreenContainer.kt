package com.joshgm3z.ping.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.PermMedia
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.InfoCard
import com.joshgm3z.common.PingBottomSheet
import com.joshgm3z.common.PingWallpaper
import com.joshgm3z.common.SheetOption
import com.joshgm3z.common.getIfNotPreview
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.data.util.getChatList
import com.joshgm3z.data.model.User
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.ping.chat.viewmodels.ChatInputUiState
import com.joshgm3z.ping.chat.viewmodels.ChatInputViewModel
import com.joshgm3z.ping.chat.viewmodels.ChatListState
import com.joshgm3z.ping.chat.viewmodels.ChatViewModel

@DarkPreview
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
    onPdfClick: (String) -> Unit = {},
    scrollToChatId: String = "",
) {
    with(viewModel.uiState.collectAsState().value) {
        you?.let {
            ChatScreen(
                chatListState = chatListState,
                scrollToChatId = scrollToChatId,
                onPdfClick = onPdfClick,
                user = it,
                onUserInfoClick = { onUserInfoClick(you.docId) },
                onBackClick = {
                    viewModel.onScreenExit()
                    goHome()
                },
                onImageClick = onImageClick,
            )
        }
    }
}


@Composable
fun ChatScreen(
    chatListState: ChatListState,
    user: User = randomUser(),
    onUserInfoClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onImageClick: (String) -> Unit = { },
    onPdfClick: (String) -> Unit = { },
    scrollToChatId: String = "",
    viewModel: ChatViewModel? = getIfNotPreview { hiltViewModel() },
    inputViewModel: ChatInputViewModel? = getIfNotPreview { hiltViewModel() },
) {
    inputViewModel?.otherGuy = user

    var showSheet by remember { mutableStateOf(false) }
    var sheetClick by remember { mutableStateOf<PingSheetClick>(PingSheetClick.Empty) }
    val pingSheetState = PingSheetState(
        show = { showSheet = it },
        click = sheetClick
    )
    val sheetList = listOf(
        SheetOption("Camera", onClick = {
            showSheet = false
            sheetClick = PingSheetClick.Camera
        }),
        SheetOption("Gallery",
            Icons.Default.PermMedia,
            onClick = {
                showSheet = false
                sheetClick = PingSheetClick.Gallery
            }),
        SheetOption("File",
            Icons.Default.AttachFile,
            onClick = {
                showSheet = false
                sheetClick = PingSheetClick.File
            }),
    )

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
                viewModel = inputViewModel,
                sheetState = pingSheetState,
                onPreviewClick = {
                    when (it) {
                        is ChatInputUiState.File -> {
                            onPdfClick(it.fileUri.toString())
                        }

                        else -> {}
                    }
                }
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
                        onPdfClick = onPdfClick,
                        onReplyClick = {
                            inputViewModel?.updateReplyPreviewState(it)
                        },
                        viewModel = viewModel,
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
    if (showSheet) {
        PingBottomSheet(
            title = "Send in chat",
            sheetOptions = sheetList,
            onDismiss = { showSheet = false }
        )
    }
}


