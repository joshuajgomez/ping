package com.joshgm3z.ping.graph

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.joshgm3z.ping.ui.screens.settings.ProfileSettings
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.screens.settings.SignOutSetting
import com.joshgm3z.ping.ui.screens.settings.UserInfo
import com.joshgm3z.ping.ui.screens.settings.image.IconPicker
import com.joshgm3z.ping.ui.screens.settings.image.ImagePickerContainer

fun NavGraphBuilder.settingGraph(
    navController: NavHostController,
    onBackClick: () -> Unit = { navController.goBack() },
) {
    navigation<SettingRoute>(startDestination = Profile) {
        composable<Profile>(enterTransition = slideIn) {
            ProfileSettings(
                onGoBackClick = onBackClick,
                openImagePicker = { navController.navigate(ImagePicker) })
        }
        composable<ImagePicker>(enterTransition = slideIn) {
            ImagePickerContainer(
                closePicker = onBackClick,
                openIconPicker = { navController.navigate(IconPicker) }
            )
        }
        composable<IconPicker>(enterTransition = slideIn) {
            IconPicker(
                goBack = onBackClick,
            )
        }
        composable<UserInfo>(enterTransition = slideIn) {
            UserInfo(onGoBackClick = onBackClick)
        }
        composable<Chat>(enterTransition = slideIn) {
            SettingContainer("Chat Settings", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<Notifications>(enterTransition = slideIn) {
            SettingContainer("Notifications", onCloseClick = onBackClick) {
                Text("Sample setting")
            }
        }
        composable<Credits>(enterTransition = slideIn) {
            SettingContainer("Credits", onCloseClick = onBackClick) {
                Text("Who do you think made all this ?")
            }
        }
        composable<SignOut>(enterTransition = slideIn) {
            SignOutSetting(
                onBackClick = onBackClick,
                onLoggedOut = navController::navigateToGoodBye,
            )
        }
    }
}