package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Output
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.screens.home.HomeAppBarContainer
import com.joshgm3z.ping.ui.screens.home.PingBottomAppBar
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.utils.Logger
import kotlinx.serialization.Serializable

@DarkPreview
@Composable
fun PreviewMainSettingsScreen() {
    PingTheme {
        Scaffold(
            topBar = {
                HomeAppBarContainer("Settings")
            },
            bottomBar = {
                PingBottomAppBar()
            },
        ) { paddingValues ->
            MainSettingsScreen(modifier = Modifier.padding(paddingValues))
        }
    }
}

@Serializable
sealed class SettingsNav {

    @Serializable
    data object Profile : SettingsNav()

    @Serializable
    data object Chat : SettingsNav()

    @Serializable
    data object Notifications : SettingsNav()

    @Serializable
    data object Account : SettingsNav()

    @Serializable
    data object Storage : SettingsNav()

    @Serializable
    data object SignOut : SettingsNav()
}

@Composable
fun MainSettingsScreen(
    modifier: Modifier = Modifier,
    onSettingNavigate: (setting: SettingsNav) -> Unit = {},
) {
    val settingList = listOf(
        Setting(
            "Profile",
            "Edit profile details"
        ) {
            onSettingNavigate(SettingsNav.Profile)
        },
        Setting(
            "Chat Settings",
            "Change chat settings"
        ) {
            onSettingNavigate(SettingsNav.Chat)
        },
        Setting(
            "Notifications",
            "Modify how often you wanna be notified"
        ) {
            onSettingNavigate(SettingsNav.Notifications)
        },
        Setting(
            "Storage",
            "See how ping is using phones memory"
        ) {
            onSettingNavigate(SettingsNav.Storage)
        },
        Setting(
            "Sign out",
            "Sign out from ping profile",
            icon = Icons.Default.Output
        ) {
            onSettingNavigate(SettingsNav.SignOut)
        },
    )

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        ProfileView()
        Spacer(Modifier.height(20.dp))
        SettingListCard(settingList)
    }
}

@Composable
fun ProfileView(
    userViewModel: UserViewModel? = getIfNotPreview { hiltViewModel() }
) {
    userViewModel?.updateCurrentUser()
    val user = when {
        userViewModel == null -> randomUser()
        else -> userViewModel.me
    }
    Logger.debug(user.toString())
    Column(
        Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserImage(
            imageUrl = user.imagePath,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
        )
        Spacer(Modifier.height(20.dp))
        Text(
            user.name, color = colorScheme.onSurface, fontSize = 25.sp
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "${user.name}@google.com",
            color = colorScheme.onSurfaceVariant,
        )
    }
}