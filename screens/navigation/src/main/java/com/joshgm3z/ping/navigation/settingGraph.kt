package com.joshgm3z.ping.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.joshgm3z.ping.ui.screens.settings.EditScreen
import com.joshgm3z.ping.ui.screens.settings.ProfileSettings
import com.joshgm3z.common.SettingContainer
import com.joshgm3z.ping.ui.screens.settings.SignOutSetting
import com.joshgm3z.ping.ui.screens.settings.UserInfo
import com.joshgm3z.ping.ui.screens.settings.image.ImagePickerContainer
import com.joshgm3z.ping.ui.screens.settings.navigateToEditScreen

fun NavGraphBuilder.settingGraph(
    navController: NavHostController,
    onBackClick: () -> Unit = { navController.goBack() },
) {
    navigation<SettingRoute>(startDestination = com.joshgm3z.ping.navigation.Profile) {
        composable<Profile>(enterTransition = slideIn) {
            ProfileSettings(
                onGoBackClick = onBackClick,
                openImagePicker = { navController.navigate(com.joshgm3z.ping.navigation.ImagePicker) },
                openEditScreen = navController::navigateToEditScreen,
            )
        }
        composable<ImagePicker>(enterTransition = slideIn) {
            ImagePickerContainer(
                closePicker = onBackClick,
            )
        }
        composable<UserInfo>(enterTransition = slideIn) {
            UserInfo(
                onGoBackClick = onBackClick,
                showClearSuccess = {
                    navController.navigate(
                        PingDialogRoute(
                            "Clear chats",
                            "Chats with this user is cleared"
                        )
                    )
                },
                showClearError = {
                    navController.navigate(
                        PingDialogRoute(
                            "Clear chats",
                            "Failed to clear chats"
                        )
                    )
                },
            )
        }
        composable<ChatSettings>(enterTransition = slideIn) {
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
        composable<EditScreen>(enterTransition = slideIn) {
            EditScreen(goBack = onBackClick)
        }
        composable<SignOut>(enterTransition = slideIn) {
            SignOutSetting(
                onBackClick = onBackClick,
                onLoggedOut = {
                    navController.navigate(com.joshgm3z.ping.navigation.Frx) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}