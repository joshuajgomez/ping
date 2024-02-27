package com.joshgm3z.ping.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.ping.model.data.HomeChat
import com.joshgm3z.ping.ui.theme.Comfortaa
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.utils.getHomeChatList
import com.joshgm3z.ping.ui.viewmodels.HomeUiState
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel

@Composable
fun HomeScreenContainer(
    homeViewModel: HomeViewModel,
    onSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onChatClick: (homeChat: HomeChat) -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        HomeTitle(
            onSearchClick = { onSearchClick() },
            onSettingsClick = { onSettingsClick() }
        )
        val uiState = homeViewModel.uiState.collectAsState()
        when (uiState.value) {
            is HomeUiState.Empty -> EmptyScreenContainer()
            is HomeUiState.Ready -> HomeChatListContainer(
                homeChats = (uiState.value as HomeUiState.Ready).homeChats,
                onChatClick = { onChatClick(it) },
            )
        }
    }
}

@Preview
@Composable
fun PreviewHomeTitle() {
    PingTheme {
        HomeTitle()
    }
}

@Preview
@Composable
fun EmptyScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Outlined.Chat,
            contentDescription = "no chats",
            modifier = Modifier.size(50.dp),
            tint = colorScheme.outline
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
            text = "Press the search icon \nto find others in ping.",
            fontSize = 16.sp,
            color = colorScheme.outline
        )
    }
}

@Composable
@Preview
fun PreviewEmptyScreen() {
    PingTheme {
        Column {
            HomeTitle()
            EmptyScreenContainer()
        }
    }
}

@Composable
fun EmptyScreenContainer() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmptyScreen()
    }
}

@Composable
fun HomeTitle(
    onSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    ConstraintLayout(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .background(color = colorScheme.surface)
    ) {
        val (search, title, setting) = createRefs()
        IconButton(
            onClick = { onSearchClick() },
            modifier = Modifier
                .constrainAs(search) {
                    start.linkTo(parent.start, margin = 15.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "search",
                tint = colorScheme.outline,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(30.dp)
            )
        }
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
        IconButton(
            onClick = { onSettingsClick() },
            modifier = Modifier
                .constrainAs(setting) {
                    end.linkTo(parent.end, margin = 15.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "search",
                tint = colorScheme.outline,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(30.dp)
            )
        }

    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    PingTheme {
        Column {
            HomeTitle()
            HomeChatListContainer()
        }
    }
}

@Composable
fun HomeChatListContainer(
    homeChats: List<HomeChat> = getHomeChatList(),
    onChatClick: (homeChat: HomeChat) -> Unit = {},
) {
    HomeChatList(homeChats) {
        onChatClick(it)
    }
}