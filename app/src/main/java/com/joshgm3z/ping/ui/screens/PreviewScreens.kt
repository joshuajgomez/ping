package com.joshgm3z.ping.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.LoadingContainer
import com.joshgm3z.data.util.getChatList
import com.joshgm3z.data.util.getHomeChatList
import com.joshgm3z.ping.chat.ChatScreen
import com.joshgm3z.ping.chat.ImagePreview
import com.joshgm3z.ping.chat.ImageViewer
import com.joshgm3z.frx.FrxContainer
import com.joshgm3z.ping.ui.screens.home.HomeAppBar
import com.joshgm3z.ping.ui.screens.home.HomeChatList
import com.joshgm3z.ping.ui.screens.home.PingBottomAppBar
import com.joshgm3z.ping.ui.screens.settings.MainSettingsScreen
import com.joshgm3z.ping.ui.screens.settings.ProfileSettings
import com.joshgm3z.ping.ui.screens.settings.SignOutSetting
import com.joshgm3z.ping.ui.screens.settings.UserInfoContent
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.ping.chat.viewmodels.ChatListState
import com.joshgm3z.ping.ui.viewmodels.HomeUiState
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

@Preview
@Composable
fun PreviewHomeScreenList() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
            bottomBar = { PingBottomAppBar() },
        ) {
            HomeChatList(
                modifier = Modifier.padding(it),
                uiState = MutableStateFlow(HomeUiState.Ready(getHomeChatList()))
            )
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreenListEmpty() {
    PingTheme {
        Scaffold(
            topBar = { HomeAppBar() },
            bottomBar = { PingBottomAppBar() },
        ) {
            HomeChatList(
                modifier = Modifier.padding(it),
                uiState = MutableStateFlow(HomeUiState.Empty())
            )
        }
    }
}

@Preview
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

@Preview
@Composable
private fun PreviewChatScreen() {
    PingTheme {
        com.joshgm3z.ping.chat.ChatScreen(
            chatListState = com.joshgm3z.ping.chat.viewmodels.ChatListState.Ready(getChatList()),
        )
    }
}

@DarkPreview
@Composable
private fun PreviewChatScreenEmpty() {
    PingTheme {
        com.joshgm3z.ping.chat.ChatScreen(chatListState = com.joshgm3z.ping.chat.viewmodels.ChatListState.Empty)
    }
}

@DarkPreview
@Composable
private fun PreviewChatScreenLoading() {
    PingTheme {
        com.joshgm3z.ping.chat.ChatScreen(chatListState = com.joshgm3z.ping.chat.viewmodels.ChatListState.Loading)
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

@Preview
@Composable
private fun PreviewLoadingContainer() {
    PingTheme {
        LoadingContainer()
    }
}