package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.joshgm3z.data.model.User
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.screens.home.HomeAppBarContainer
import com.joshgm3z.ping.ui.screens.home.PingBottomAppBar
import com.joshgm3z.ping.ui.screens.settings.image.ImagePickerContainer
import com.joshgm3z.ping.ui.screens.settings.image.getUserViewModel
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.utils.Logger
import kotlinx.serialization.Serializable

@DarkPreview
@Composable
fun PreviewSettingsScreen() {
    PingTheme {
        Scaffold(
            topBar = {
                HomeAppBarContainer("Settings")
            },
            bottomBar = {
                PingBottomAppBar()
            },
        ) { paddingValues ->
            SettingsScreen(modifier = Modifier.padding(paddingValues))
        }
    }
}

@Serializable
data class Setting(
    val title: String,
    val subTitle: String = "",
    val nav: SettingsNav,
)

val settings = listOf(
    Setting("Profile", "Edit profile details", SettingsNav.Profile),
    Setting("Chat Settings", "Change chat settings", SettingsNav.Chat),
    Setting("Notifications", "Modify how often you wanna be notified", SettingsNav.Notifications),
    Setting("Account", "Change account settings", SettingsNav.Account),
    Setting("Storage", "See how ping is using phones memory", SettingsNav.Storage),
    Setting("Sign out", "Sign out from ping profile", SettingsNav.SignOut),
)

@Serializable
sealed class SettingsNav {
    @Serializable
    data object Home : SettingsNav()

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
fun SettingScreenContainer(
    startDestination: SettingsNav,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = hiltViewModel(),
    onLoggedOut: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<SettingsNav.Profile> {
            ImagePickerContainer(onGoBackClick = onBackClick)
        }
        composable<SettingsNav.Chat> {
            SettingContainer("Chat Settings", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<SettingsNav.Notifications> {
            SettingContainer("Notifications", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<SettingsNav.Account> {
            SettingContainer("Account", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<SettingsNav.Storage> {
            SettingContainer("Storage", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<SettingsNav.SignOut> {
            SignOutSetting(
                onBackClick = onBackClick,
                onSignOutClick = {
                    userViewModel.onSignOutClicked {
                        onLoggedOut()
                    }
                })
        }
    }
}


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel? = getUserViewModel(),
    onSettingNavigate: (setting: SettingsNav) -> Unit = {},
) {
    val user = when {
        userViewModel == null -> randomUser()
        else -> userViewModel.me
    }
    Column(modifier.fillMaxSize()) {
        ProfileView(user)
        Spacer(Modifier.height(20.dp))
        ElevatedCard(
            modifier = Modifier.padding(horizontal = 15.dp),
            colors = CardDefaults.elevatedCardColors().copy(
                containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
            )
        ) {
            LazyColumn {
                itemsIndexed(settings) { index, item ->
                    SettingItem(item, { onSettingNavigate(item.nav) })
                    if (index < settings.lastIndex) HorizontalDivider(thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    setting: Setting,
    onSettingClick: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onSettingClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(setting.title, fontSize = 18.sp, color = colorScheme.onSurface)
        Spacer(Modifier.height(5.dp))
        Text(setting.subTitle, fontSize = 15.sp, color = colorScheme.onSurface.copy(alpha = 0.7f))
    }
}

@Composable
fun ProfileView(user: User) {
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logger.debug(user.imagePath)
        AsyncImage(
            model = user.imagePath,
            placeholder = painterResource(R.drawable.default_user),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            onError = {
                Logger.error(it.toString())
            },
            onSuccess = {
                Logger.info(it.toString())
            },
        )
        Spacer(Modifier.height(20.dp))
        Text(
            user.name,
            color = colorScheme.onSurface,
            fontSize = 25.sp
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "${user.name}@google.com",
            color = colorScheme.onSurfaceVariant,
        )
    }
}