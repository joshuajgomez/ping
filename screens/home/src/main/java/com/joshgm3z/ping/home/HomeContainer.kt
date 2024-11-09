package com.joshgm3z.ping.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.HomeAppBar
import com.joshgm3z.common.navigation.ChatScreen
import com.joshgm3z.search.UserList
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.ping.home.viewmodels.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow

@DarkPreview
@Composable
private fun PreviewHomeScreenList() {
    PingTheme {
        Scaffold(topBar = { HomeAppBar() }) {
            HomeChatList(modifier = Modifier.padding(it))
        }
    }
}

@DarkPreview
@Composable
private fun PreviewHomeScreenListEmpty() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
        ) {
            HomeChatList(
                modifier = Modifier.padding(it),
                uiState = MutableStateFlow(HomeUiState.Empty())
            )
        }
    }
}

@DarkPreview
@Composable
fun PreviewHomeScreenUsers() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
        ) {
            UserList(modifier = Modifier.padding(it))
        }
    }
}

@Composable
fun HomeScreenContainer(
    navigateTo: (Any) -> Unit,
) {
    Scaffold(
        topBar = { HomeAppBar(navigateTo) },
    ) { paddingValues ->
        Surface(Modifier.padding(paddingValues)) {
            HomeChatListContainer(
                onChatClick = { chat ->
                    navigateTo(ChatScreen(chat.otherGuy.docId))
                },
            )
        }
    }
}

@DarkPreview
@Composable
fun NavIcon(
    imageVector: ImageVector = Icons.Rounded.ChatBubble,
    isSelected: Boolean = false,
    onNavIconClick: () -> Unit = {},
) {
    IconButton(onClick = { onNavIconClick() }) {
        Icon(
            imageVector = imageVector, contentDescription = null,
            tint = if (isSelected) colorScheme.primaryContainer else colorScheme.outline
        )
    }
}
