package com.joshgm3z.common.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class HomeRoute(
    @Transient val icon: ImageVector = Icons.Rounded.QuestionMark,
) {
    @Serializable
    data object ChatList : HomeRoute(Icons.Rounded.ChatBubble)

    @Serializable
    data object UserList : HomeRoute(Icons.Rounded.Group)

    @Serializable
    data object SettingsList : HomeRoute(Icons.Rounded.Settings)
}

val homeNavItems = listOf(
    HomeRoute.ChatList,
    HomeRoute.UserList,
    HomeRoute.SettingsList,
)