package com.joshgm3z.ping.ui.screens.frx

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MarkChatUnread
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.LoadingContainer
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.WelcomeViewModel

@DarkPreview
@Composable
fun PreviewWelcomeScreen() {
    PingTheme {
        WelcomeScreen()
    }
}

@Composable
fun WelcomeScreen(
    name: String = "Alien",
    viewModel: WelcomeViewModel? = getIfNotPreview { hiltViewModel() },
    onUserSyncComplete: () -> Unit = {},
) {
    viewModel?.downloadUserList(onUserSyncComplete)
    LoadingContainer(
        "Hello $name!",
        "Please wait for a sec",
        Icons.Outlined.MarkChatUnread
    )
}