package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.screens.settings.image.ImagePickerContainer
import com.joshgm3z.ping.ui.viewmodels.UserViewModel

@Composable
fun SettingScreenContainer(
    startDestination: SettingsNav,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel? = getIfNotPreview { hiltViewModel() },
    onLoggedOut: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
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
            SignOutSetting(onBackClick = onBackClick, onSignOutClick = {
                userViewModel?.onSignOutClicked {
                    onLoggedOut()
                }
            })
        }
    }
}