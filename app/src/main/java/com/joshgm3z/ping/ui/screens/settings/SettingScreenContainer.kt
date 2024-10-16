package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.screens.settings.image.ImagePickerContainer
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import kotlinx.serialization.Serializable

@Composable
fun SettingScreenContainer(
    startRoute: SettingsRoute,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel? = getIfNotPreview { hiltViewModel() },
    onLoggedOut: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startRoute,
        modifier = modifier
    ) {
        composable<SettingsRoute.Profile> {
            ProfileSettings(
                onGoBackClick = onBackClick,
                openImagePicker = { navController.navigate(SettingsRoute.ImagePicker) })
        }
        composable<SettingsRoute.ImagePicker> {
            ImagePickerContainer(onGoBackClick = { navController.popBackStack() })
        }
        composable<SettingsRoute.UserInfo> {
            val userId = it.toRoute<SettingsRoute.UserInfo>().userId
            UserInfo(userId, onGoBackClick = onBackClick)
        }
        composable<SettingsRoute.Chat> {
            SettingContainer("Chat Settings", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<SettingsRoute.Notifications> {
            SettingContainer("Notifications", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<SettingsRoute.Account> {
            SettingContainer("Account", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<SettingsRoute.Storage> {
            SettingContainer("Storage", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<SettingsRoute.SignOut> {
            SignOutSetting(onBackClick = onBackClick, onSignOutClick = {
                userViewModel?.onSignOutClicked {
                    onLoggedOut()
                }
            })
        }
    }
}

@Serializable
sealed class SettingsRoute {

    @Serializable
    data object Profile : SettingsRoute()

    @Serializable
    data class UserInfo(val userId: String) : SettingsRoute()

    @Serializable
    data object ImagePicker : SettingsRoute()

    @Serializable
    data object Chat : SettingsRoute()

    @Serializable
    data object Notifications : SettingsRoute()

    @Serializable
    data object Account : SettingsRoute()

    @Serializable
    data object Storage : SettingsRoute()

    @Serializable
    data object SignOut : SettingsRoute()
}