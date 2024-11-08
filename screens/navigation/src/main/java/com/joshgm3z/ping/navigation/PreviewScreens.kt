package com.joshgm3z.ping.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.HomeAppBar
import com.joshgm3z.common.LoadingContainer
import com.joshgm3z.common.home.PingBottomAppBar
import com.joshgm3z.frx.FrxContainer
import com.joshgm3z.ping.home.HomeChatList
import com.joshgm3z.settings.MainSettingsScreen
import com.joshgm3z.settings.ProfileSettings
import com.joshgm3z.settings.SignOutSetting
import com.joshgm3z.settings.UserInfoContent
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.data.util.getChatList
import com.joshgm3z.ping.chat.viewmodels.ChatListState
import kotlinx.coroutines.flow.MutableStateFlow

@DarkPreview
@Composable
private fun PreviewFrxContainer() {
    PingTheme {
        FrxContainer(true)
    }
}

@DarkPreview
@Composable
private fun PreviewFrxContainer2() {
    PingTheme {
        FrxContainer(false)
    }
}

@DarkPreview
@Composable
fun PreviewHomeScreenList() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
            bottomBar = { PingBottomAppBar() },
        ) {
            HomeChatList(
                modifier = Modifier.padding(it),
            )
        }
    }
}

@DarkPreview
@Composable
fun PreviewHomeScreenListEmpty() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
            bottomBar = { PingBottomAppBar() },
        ) {
            HomeChatList(
                modifier = Modifier.padding(it),
                uiState = MutableStateFlow(com.joshgm3z.ping.home.viewmodels.HomeUiState.Empty())
            )
        }
    }
}

@DarkPreview
@Composable
fun PreviewHomeScreenSettings() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
            bottomBar = { PingBottomAppBar() },
        ) {
            MainSettingsScreen(modifier = Modifier.padding(it))
        }
    }
}

@DarkPreview
@Composable
private fun PreviewChatScreen() {
    PingTheme {
        com.joshgm3z.ping.chat.ChatScreen(
            chatListState = ChatListState.Ready(getChatList()),
        )
    }
}

@DarkPreview
@Composable
private fun PreviewChatScreenEmpty() {
    PingTheme {
        com.joshgm3z.ping.chat.ChatScreen(chatListState = ChatListState.Empty)
    }
}

@DarkPreview
@Composable
private fun PreviewChatScreenLoading() {
    PingTheme {
        com.joshgm3z.ping.chat.ChatScreen(chatListState = ChatListState.Loading)
    }
}

@DarkPreview
@Composable
private fun PreviewUserInfo() {
    PingTheme {
        UserInfoContent(mediaChats = getChatList())
    }
}

@DarkPreview
@Composable
private fun PreviewImagePreview() {
    PingTheme {
        com.joshgm3z.ping.chat.ImagePreview()
    }
}

@DarkPreview
@Composable
private fun PreviewImageViewer() {
    PingTheme {
        com.joshgm3z.ping.chat.ImageViewer()
    }
}

@DarkPreview
@Composable
private fun PreviewProfileSettings() {
    PingTheme {
        ProfileSettings()
    }
}

@DarkPreview
@Composable
private fun PreviewSignOutSetting() {
    PingTheme {
        SignOutSetting()
    }
}

@DarkPreview
@Composable
private fun PreviewLoadingContainer() {
    PingTheme {
        LoadingContainer()
    }
}