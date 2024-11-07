package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.getIfNotPreview
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.data.model.Chat
import com.joshgm3z.ping.ui.viewmodels.ChatInlineUiState
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel
import com.joshgm3z.ping.utils.getPrettyTime
import kotlinx.coroutines.launch

val chatBubbleRadius = 10.dp

@Composable
fun ChatList(
    chats: List<Chat> = emptyList(),
    onImageClick: (chatId: String) -> Unit = {},
    onPdfClick: (String) -> Unit = {},
    onReplyClick: (Chat) -> Unit = {},
    scrollToChatId: String = "",
    viewModel: ChatViewModel? = getIfNotPreview { hiltViewModel() }
) {
    Box(contentAlignment = Alignment.BottomCenter) {
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        LazyColumn(
            state = listState,
            reverseLayout = true,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(items = chats) { chat ->
                ChatItem(
                    chat = chat,
                    onClick = { uiState ->
                        when (uiState) {
                            is ChatInlineUiState.Image -> onImageClick(chat.docId)
                            is ChatInlineUiState.Reply -> {
                                scope.launch {
                                    listState.scrollToItem(
                                        chats.indexOfFirst { it.docId == uiState.chat.docId }
                                    )
                                }
                            }

                            is ChatInlineUiState.File -> {
                                if (uiState.chat.fileLocalUri.isNotEmpty()) {
                                    onPdfClick(uiState.chat.fileLocalUri)
                                }
                            }

                            else -> {}
                        }
                    },
                    onSwipeRight = {
                        onReplyClick(chat)
                    },
                    viewModel = viewModel,
                )
            }
        }
        listState.ScrollIfNeeded(scrollToChatId, chats)
        val coroutineScope = rememberCoroutineScope()
        val visibleIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }
        ScrollButton(visible = visibleIndex.value > 5) {
            coroutineScope.launch {
                listState.scrollToItem(0)
            }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    chat: Chat,
    onClick: (ChatInlineUiState) -> Unit = {},
    onLongClick: (ChatInlineUiState) -> Unit = {},
    onSwipeRight: () -> Unit = {},
    viewModel: ChatViewModel? = getIfNotPreview { hiltViewModel() },
    defaultUiStateForPreview: ChatInlineUiState = ChatInlineUiState.Empty,
) {
    val previewState = viewModel?.getChatPreviewState(chat) ?: defaultUiStateForPreview
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onSwipeRight()
                }

                else -> {}
            }
            false
        }
    )
    val interactionSource = remember { MutableInteractionSource() }
    SwipeToDismissBox(
        state = state,
        backgroundContent = {},
        enableDismissFromEndToStart = false
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxWidth()
                .combinedClickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onLongClick = { onLongClick(previewState) },
                    onClick = { onClick(previewState) }
                ),
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
                InlinePreview(chat, previewState)
                Message(chat)
            }
            Details(chat)
        }
    }
}

@Composable
fun InlinePreview(
    chat: Chat,
    uiState: ChatInlineUiState,
) {
    if (uiState is ChatInlineUiState.Empty) return
    Column(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(chatBubbleRadius))
            .background(
                color = when {
                    chat.isOutwards -> colorScheme.surfaceContainerHigh
                    else -> colorScheme.primaryContainer.copy(alpha = 0.9f)
                }
            )
    ) {
        InlinePreviewContent(uiState)
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

//@Preview
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

@DarkPreview
@Composable
fun PreviewOutgoingChatReplyOutwards(outwards: Boolean = true) {
    PingTheme {
        val chat = Chat.random("me too")
        Column {
            ChatItem(chat.apply {
                isOutwards = outwards
            })
            ChatItem(
                chat.apply {
                    isOutwards = outwards
                },
                defaultUiStateForPreview = ChatInlineUiState.Reply(Chat.random(), "Alien")
            )
            ChatItem(
                chat.apply {
                    isOutwards = outwards
                },
                defaultUiStateForPreview = ChatInlineUiState.FileUpload(
                    chat.apply {
                        fileType = "exe"
                        fileLocalUriToUpload = "1111"
                        fileName = "qqww"
                    }
                )
            )
            ChatItem(
                chat.apply {
                    isOutwards = outwards
                },
                defaultUiStateForPreview = ChatInlineUiState.File(chat.apply {
                    fileType = "pdf"
                    fileOnlineUrl = "1122"
                    fileName = "Yoyo.pdf"
                })
            )
            ChatItem(
                chat.apply {
                    isOutwards = outwards
                },
                defaultUiStateForPreview = ChatInlineUiState.File(chat.apply {
                    fileType = "exe"
                    fileOnlineUrl = "11"
                })
            )
            ChatItem(
                Chat("Hey wassup my maan hows it going").apply {
                    isOutwards = outwards
                },
                defaultUiStateForPreview = ChatInlineUiState.Reply(Chat("ok"), "Alien")
            )
            ChatItem(
                chat.apply {
                    isOutwards = outwards
                    fileOnlineUrl = "im going to movie"
                },
                defaultUiStateForPreview = ChatInlineUiState.Image(chat.apply {
                    fileType = "jpg"
                    fileOnlineUrl = "qq"
                })
            )
            ChatItem(
                chat.apply {
                    isOutwards = outwards
                },
                defaultUiStateForPreview = ChatInlineUiState.ImageUpload(chat.apply {
                    fileType = "jpg"
                    fileLocalUriToUpload = "qq"
                })
            )
            ChatItem(
                Chat("").apply {
                    isOutwards = outwards
                },
                defaultUiStateForPreview = ChatInlineUiState.Image(chat.apply {
                    fileType = "jpg"
                    fileOnlineUrl = "ww"
                })
            )
            ChatItem(
                Chat("https://www.google.com").apply {
                    isOutwards = outwards
                },
                defaultUiStateForPreview = ChatInlineUiState.WebUrl("www.google.com")
            )
            ChatItem(chat.apply {
                isOutwards = outwards
            })
        }
    }
}

@DarkPreview
@Composable
fun PreviewOutgoingChatReply() {
    PreviewOutgoingChatReplyOutwards(false)
}
