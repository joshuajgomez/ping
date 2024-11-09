package com.joshgm3z.ping.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.joshgm3z.settings.EditScreen
import com.joshgm3z.settings.ProfileSettings
import com.joshgm3z.common.SettingContainer
import com.joshgm3z.common.navigation.ChatSettings
import com.joshgm3z.common.navigation.Credits
import com.joshgm3z.common.navigation.EditScreenRoute
import com.joshgm3z.common.navigation.Frx
import com.joshgm3z.common.navigation.ImagePicker
import com.joshgm3z.common.navigation.Notifications
import com.joshgm3z.common.navigation.PingDialogRoute
import com.joshgm3z.common.navigation.Profile
import com.joshgm3z.common.navigation.SettingRoute
import com.joshgm3z.common.navigation.SettingsHome
import com.joshgm3z.common.navigation.SignOut
import com.joshgm3z.common.navigation.UserInfoRoute
import com.joshgm3z.settings.MainSettingsScreen
import com.joshgm3z.settings.SignOutSetting
import com.joshgm3z.settings.UserInfo
import com.joshgm3z.settings.image.ImagePickerContainer
import com.joshgm3z.settings.navigateToEditScreen

fun NavGraphBuilder.settingGraph(
    navController: NavHostController,
    onBackClick: () -> Unit = { navController.goBack() },
) {
    navigation<SettingRoute>(startDestination = SettingsHome) {
        composable<SettingsHome>(enterTransition = slideIn) {
            MainSettingsScreen {
                navController.navigate(it)
            }
        }
        composable<Profile>(enterTransition = slideIn) {
            ProfileSettings(
                onGoBackClick = onBackClick,
                openImagePicker = { navController.navigate(ImagePicker) },
                openEditScreen = navController::navigateToEditScreen,
            )
        }
        composable<ImagePicker>(enterTransition = slideIn) {
            ImagePickerContainer(
                closePicker = onBackClick,
            )
        }
        composable<UserInfoRoute>(enterTransition = slideIn) {
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
        composable<EditScreenRoute>(enterTransition = slideIn) {
            EditScreen(goBack = onBackClick)
        }
        composable<SignOut>(enterTransition = slideIn) {
            SignOutSetting(
                onBackClick = onBackClick,
                onLoggedOut = {
                    navController.navigate(Frx) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
            )
        }
    }
}