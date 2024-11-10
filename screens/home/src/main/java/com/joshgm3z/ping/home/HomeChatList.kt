package com.joshgm3z.ping.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.HomeAppBar
import com.joshgm3z.common.home.HomeChatItem
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.data.util.getHomeChatList
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.HomeChat
import com.joshgm3z.common.theme.Gray60
import com.joshgm3z.ping.home.viewmodels.HomeUiState
import com.joshgm3z.ping.home.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

//@DarkPreview
@Composable
fun PreviewHomeChatList() {
    PingTheme {
        PingTheme {
            Scaffold(
                topBar = {
                    HomeAppBar()
                },
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    HomeChatList(uiState = MutableStateFlow(HomeUiState.Ready(getHomeChatList())))
                }
            }
        }
    }
}

@DarkPreview
@Composable
fun PreviewHomeSentChat(isOutwards: Boolean = true) {
    PingTheme {
        val homeChat = HomeChat.random()
        homeChat.lastChat.status = Chat.READ
        homeChat.lastChat.isOutwards = isOutwards
        homeChat.lastChat.fileOnlineUrl = "true"
        HomeChatItem(homeChat)
    }
}

@DarkPreview
@Composable
fun PreviewHomeReceivedChat() {
    PingTheme {
        val homeChat = HomeChat.random()
        homeChat.lastChat.isOutwards = false
        homeChat.lastChat.fileOnlineUrl = "true"
        HomeChatItem(homeChat)
    }
}

@DarkPreview
@Composable
fun PreviewHomeReceivedChat2() {
    PingTheme {
        val homeChat = HomeChat.random()
        homeChat.lastChat.message = ""
        homeChat.lastChat.isOutwards = false
        homeChat.lastChat.fileName = "Some file.pdf"
        homeChat.lastChat.fileType = "pdf"
        HomeChatItem(homeChat)
    }
}

@DarkPreview
@Composable
fun PreviewHomeReceivedChat3() {
    PingTheme {
        val homeChat = HomeChat.random()
        homeChat.lastChat.message = ""
        homeChat.lastChat.isOutwards = false
        homeChat.lastChat.fileName = "Some file.png"
        homeChat.lastChat.fileType = "png"
        HomeChatItem(homeChat)
    }
}

@Composable
fun HomeChatListContainer(
    viewModel: HomeViewModel = hiltViewModel(),
    onChatClick: (homeChat: HomeChat) -> Unit = {},
    onGoToUsersClicked: () -> Unit = {},
) {
    HomeChatList(
        uiState = viewModel.uiState,
        onChatClick = onChatClick,
        onGoToUsersClicked = onGoToUsersClicked,
    )
}

@Composable
fun HomeChatList(
    modifier: Modifier = Modifier,
    uiState: StateFlow<HomeUiState> = MutableStateFlow(HomeUiState.Empty()),
    onChatClick: (homeChat: HomeChat) -> Unit = {},
    onGoToUsersClicked: () -> Unit = {},
) {
    Column(modifier) {
        Spacer(Modifier.size(10.dp))
        with(uiState.collectAsState().value) {
            when (this) {
                is HomeUiState.Ready -> {
                    LazyColumn {
                        items(items = homeChats) { it ->
                            HomeChatItem(it) {
                                onChatClick(it)
                            }
                        }
                    }
                }

                else -> EmptyScreen(onGoToUsersClicked)
            }

        }
    }
}

@Composable
fun SearchBoxView(onClick: () -> Unit, modifier: Modifier, hintText: String) {
    Text(
        hintText,
        modifier
            .clip(shape = RoundedCornerShape(30.dp))
            .clickable { onClick() }
            .background(Gray60)
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp),
        color = colorScheme.onSurface.copy(alpha = 0.4f)
    )
}

//@Preview
@Composable
fun PreviewEmptyScreen() {
    PingTheme {
        EmptyScreen()
    }
}

@Composable
fun EmptyScreen(onGoToUsersClicked: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Filled.Forum,
            contentDescription = "no chats",
            modifier = Modifier.size(50.dp),
            tint = colorScheme.primary
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Start pinging!",
            fontSize = 18.sp,
            color = colorScheme.outline,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Go to users screen \nto find others in ping",
            fontSize = 16.sp,
            color = colorScheme.outline,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(30.dp))
        TextButton(
            onClick = onGoToUsersClicked
        ) {
            Text(
                "See users",
            )
        }
    }
}