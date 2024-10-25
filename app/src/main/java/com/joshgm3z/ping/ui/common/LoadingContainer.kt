package com.joshgm3z.ping.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.outlined.MarkChatRead
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.joshgm3z.ping.graph.Loading
import com.joshgm3z.ping.ui.theme.PingTheme

@Preview
@Composable
private fun PreviewLoadingContainer() {
    PingTheme {
        LoadingContainer()
    }
}

fun NavController.navigateToLoading(message: String) =
    navigate(Loading(message))

@Composable
fun LoadingContainer(
    title: String = "Loading",
    subtitle: String = "Please wait",
    icon: ImageVector = Icons.Outlined.MarkChatRead
) {
    PingWallpaper {
        InfoCard(title, subtitle, icon)
    }
}
