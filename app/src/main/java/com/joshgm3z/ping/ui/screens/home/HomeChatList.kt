package com.joshgm3z.ping.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.HomeChat
import com.joshgm3z.ping.ui.screens.chat.StatusIcon
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.HomeUiState
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel
import com.joshgm3z.ping.utils.getHomeChatList
import com.joshgm3z.ping.utils.getPrettyTime

@Preview
@Composable
fun PreviewHomeSentChat() {
    PingTheme {
        val homeChat = HomeChat.random()
        homeChat.lastChat.status = Chat.READ
        homeChat.lastChat.isOutwards = true
        HomeChatItem(homeChat)
    }
}

@Preview
@Composable
fun PreviewHomeReceivedChat() {
    PingTheme {
        val homeChat = HomeChat.random()
        homeChat.lastChat.isOutwards = false
        HomeChatItem(homeChat)
    }
}

@Composable
fun HomeChatListContainer(
    homeViewModel: HomeViewModel,
    onChatClick: (homeChat: HomeChat) -> Unit = {},
    onGoToUsersClicked: () -> Unit = {},
) {
    val uiState = homeViewModel.uiState.collectAsState()
    when (uiState.value) {
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
        val imageRes = homeChat.otherGuy.imageRes
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "default user",
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(45.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 10.dp)
                }
        )
        Text(
            text = homeChat.otherGuy.name,
            fontSize = 20.sp,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .constrainAs(user) {
                    top.linkTo(parent.top, margin = 5.dp)
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
            }) {
            AnimatedVisibility(visible = homeChat.lastChat.isOutwards) {
                StatusIcon(
                    status = homeChat.lastChat.status,
                    modifier = Modifier
                        .padding(end = 5.dp, top = 2.dp)
                        .size(17.dp)
                )
            }
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
                    top.linkTo(parent.top, margin = 10.dp)
                    end.linkTo(parent.end, margin = 10.dp)
                },
        )
        AnimatedVisibility(
            visible = homeChat.count > 0,
            modifier = Modifier.constrainAs(count) {
                top.linkTo(time.bottom, margin = 5.dp)
                end.linkTo(time.end)
            }
        ) {
            Text(
                text = "${homeChat.count}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.primary,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colorScheme.primaryContainer)
                    .padding(horizontal = 8.dp, vertical = 1.dp)
            )
        }
        Divider(
            color = colorScheme.outlineVariant.copy(alpha = 0.1f),
            modifier = Modifier.constrainAs(line) {
                top.linkTo(image.bottom, margin = 10.dp)
            })
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