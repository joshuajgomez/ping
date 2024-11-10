package com.joshgm3z.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MarkChatRead
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewLoadingContainer() {
    PingTheme {
        LoadingContainer()
    }
}

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
