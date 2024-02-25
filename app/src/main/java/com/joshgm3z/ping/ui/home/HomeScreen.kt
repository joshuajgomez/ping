package com.joshgm3z.ping.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.ping.data.HomeChat
import com.joshgm3z.ping.ui.search.EmptyScreen
import com.joshgm3z.ping.ui.theme.Comfortaa
import com.joshgm3z.ping.ui.theme.Gray40
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Purple60
import com.joshgm3z.ping.utils.getHomeChatList
import com.joshgm3z.ping.viewmodels.HomeUiState
import com.joshgm3z.ping.viewmodels.HomeViewModel

@Composable
fun HomeScreenContainer(
    homeViewModel: HomeViewModel,
    onSearchClick: () -> Unit = {},
    onChatClick: (homeChat: HomeChat) -> Unit = {},
) {
    val uiState = homeViewModel.uiState.collectAsState()
    when (uiState.value) {
        is HomeUiState.Empty -> EmptyScreen()
        is HomeUiState.Ready -> HomeScreen(
            (uiState.value as HomeUiState.Ready).homeChats,
            onSearchClick = { onSearchClick() },
            onChatClick = { onChatClick(it) },
        )
    }
}

@Preview
@Composable
fun PreviewHomeTitle() {
    PingTheme {
        HomeTitle()
    }
}

@Composable
@Preview
fun PreviewEmptyScreen() {
    PingTheme {
        EmptyScreenHome()
    }
}

@Composable
fun EmptyScreenHome() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Outlined.Chat,
            contentDescription = "no chats",
            modifier = Modifier.size(50.dp),
            tint = colorScheme.primary
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "No chats yet", fontSize = 16.sp, color = colorScheme.primary)
    }
}

@Composable
fun HomeTitle(onSearchClick: () -> Unit = {}) {
    ConstraintLayout(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .background(color = colorScheme.surface)
    ) {
        val (title, search) = createRefs()
        Text(
            text = "ping",
            color = colorScheme.onSurface,
            fontSize = 30.sp,
            fontFamily = Comfortaa,
            modifier = Modifier
                .padding(top = 5.dp)
                .constrainAs(title) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "search",
            tint = colorScheme.onSurface,
            modifier = Modifier
                .constrainAs(search) {
                    end.linkTo(parent.end, margin = 15.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clip(CircleShape)
                .size(30.dp)
                .clickable { onSearchClick() })
    }
}

@Composable
@Preview
fun HomeScreen(
    homeChats: List<HomeChat> = getHomeChatList(),
    onSearchClick: () -> Unit = {},
    onChatClick: (homeChat: HomeChat) -> Unit = {},
) {
    PingTheme {
        Column {
            HomeTitle { onSearchClick() }
            HomeChatList(homeChats) {
                onChatClick(it)
            }
        }
    }
}