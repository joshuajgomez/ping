package com.joshgm3z.ping.graph

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.joshgm3z.ping.ui.screens.settings.ProfileSettings
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.screens.settings.SignOutSetting
import com.joshgm3z.ping.ui.screens.settings.UserInfo
import com.joshgm3z.ping.ui.screens.settings.image.ImagePickerContainer

fun NavGraphBuilder.settingGraph(
    navController: NavHostController,
    onBackClick: () -> Unit = {
        navController.popBackStack()
    }
) {
    navigation<SettingRoute>(startDestination = Profile) {
        composable<Profile> {
            ProfileSettings(
                onGoBackClick = onBackClick,
                openImagePicker = { navController.navigate(ImagePicker) })
        }
        composable<ImagePicker> {
            ImagePickerContainer(onGoBackClick = onBackClick)
        }
        composable<UserInfo> {
            val userId = it.toRoute<UserInfo>().userId
            UserInfo(userId, onGoBackClick = onBackClick)
        }
        composable<Chat> {
            SettingContainer("Chat Settings", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<Notifications> {
            SettingContainer("Notifications", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<Credits> {
            SettingContainer("Credits", onCloseClick = onBackClick) {
                Text("Who do you think made all this ?")
            }
        }
        composable<SignOut> {
            SignOutSetting(
                navController,
                onBackClick = onBackClick,
            )
        }
    }
}