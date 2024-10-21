package com.joshgm3z.ping.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.screens.chat.StatusIcon
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.HomeUiState
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel
import com.joshgm3z.data.util.getHomeChatList
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.HomeChat
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.utils.getPrettyTime

@DarkPreview
@Composable
fun PreviewHomeChatList() {
    PingTheme {
        PingTheme {
            Scaffold(
                topBar = {
                    HomeAppBarContainer("Chats")
                },
                bottomBar = {
                    PingBottomAppBar()
                },
            ) { paddingValues ->
                LazyColumn(Modifier.padding(paddingValues)) {
                    items(items = getHomeChatList()) {
                        HomeChatItem(it) {}
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeSentChat() {
    PingTheme {
        val homeChat = HomeChat.random()
        homeChat.lastChat.status = Chat.READ
        homeChat.lastChat.isOutwards = true
        homeChat.lastChat.imageUrl = "true"
        HomeChatItem(homeChat)
    }
}

@Preview
@Composable
fun PreviewHomeReceivedChat() {
    PingTheme {
        val homeChat = HomeChat.random()
        homeChat.lastChat.isOutwards = false
        homeChat.lastChat.imageUrl = "true"
        HomeChatItem(homeChat)
    }
}

@Composable
fun HomeChatListContainer(
    homeViewModel: HomeViewModel? = getIfNotPreview { hiltViewModel() },
    onChatClick: (homeChat: HomeChat) -> Unit = {},
    onGoToUsersClicked: () -> Unit = {},
) {
    val uiState = homeViewModel?.uiState?.collectAsState()
    when (uiState?.value) {
        is HomeUiState.Ready -> {
            HomeChatList(
                homeChats = (uiState.value as HomeUiState.Ready).homeChats,
                onChatClick = { onChatClick(it) },
                onGoToUsersClicked = { onGoToUsersClicked() }
            )
        }

        else -> {
            EmptyScreen {
                onGoToUsersClicked()
            }
        }
    }
}

@Preview
@Composable
fun HomeChatList(
    modifier: Modifier = Modifier,
    homeChats: List<HomeChat> = getHomeChatList(),
    onChatClick: (homeChat: HomeChat) -> Unit = {},
    onGoToUsersClicked: () -> Unit = {},
) {
    if (homeChats.isNotEmpty()) {
        LazyColumn {
            items(items = homeChats) { it ->
                HomeChatItem(it) {
                    onChatClick(it)
                }
            }
        }
    } else {
        EmptyScreen(onGoToUsersClicked = { onGoToUsersClicked() })
    }
}

@Composable
fun HomeChatItem(
    homeChat: HomeChat = HomeChat.random(),
    onChatClick: (homeChat: HomeChat) -> Unit = {},
) {
    ConstraintLayout(modifier = Modifier.clickable { onChatClick(homeChat) }) {
        val (message, user, count, image, line, time) = createRefs()
        UserImage(
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(45.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 15.dp)
                },
            user = homeChat.otherGuy
        )
        Text(
            text = homeChat.otherGuy.name,
            fontSize = 20.sp,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .constrainAs(user) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(image.end, margin = 15.dp)
                }
                .widthIn(max = 260.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.constrainAs(message) {
                top.linkTo(user.bottom)
                start.linkTo(user.start)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = homeChat.lastChat.isOutwards) {
                StatusIcon(
                    status = homeChat.lastChat.status,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(17.dp)
                )
            }
            MediaIcon(homeChat)
            Text(
                text = homeChat.lastChat.message,
                color = colorScheme.onSurfaceVariant,
                fontSize = 15.sp,
                modifier = Modifier.widthIn(max = 260.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = getPrettyTime(homeChat.lastChat.sentTime),
            color = if (homeChat.count > 0) {
                colorScheme.primary
            } else {
                colorScheme.outline
            },
            fontSize = 13.sp,
            modifier = Modifier
                .constrainAs(time) {
                    top.linkTo(user.top)
                    end.linkTo(parent.end, margin = 15.dp)
                },
        )
        AnimatedVisibility(
            visible = homeChat.count > 0,
            modifier = Modifier.constrainAs(count) {
                top.linkTo(time.bottom, margin = 3.dp)
                end.linkTo(time.end)
            }
        ) {
            Text(
                text = "${homeChat.count}",
                fontSize = 15.sp,
                color = colorScheme.primary,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colorScheme.primaryContainer)
                    .padding(horizontal = 8.dp, vertical = 0.dp)
            )
        }
        HorizontalDivider(
            color = colorScheme.outlineVariant.copy(alpha = 0.4f),
            modifier = Modifier.constrainAs(line) {
                top.linkTo(image.bottom, margin = 10.dp)
            })
    }
}

@Composable
fun MediaIcon(homeChat: HomeChat) {
    if (homeChat.lastChat.imageUrl.isNotEmpty()) {
        Icon(
            Icons.Default.CameraAlt,
            contentDescription = null,
            tint = colorScheme.onSurface,
            modifier = Modifier
                .padding(end = 3.dp)
                .size(17.dp)
        )
    }
}

@Preview
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
        Button(
            onClick = { onGoToUsersClicked() }
        ) {
            Text(
                text = "Go to users",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}