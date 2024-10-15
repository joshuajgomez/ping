package com.joshgm3z.ping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joshgm3z.ping.ui.screens.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.screens.frx.FrxContainer
import com.joshgm3z.ping.ui.screens.home.HomeScreenContainer
import com.joshgm3z.ping.ui.screens.settings.SettingScreenContainer
import com.joshgm3z.ping.ui.screens.settings.SettingsNav
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.viewmodels.SignInViewModel
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.utils.Logger
import kotlinx.coroutines.launch

const val navSignIn = "signin_screen"
const val navHome = "home_screen"
const val navChat = "chat_screen"
const val navSettings = "navSettings"

class PingNavState {
    companion object {
        var currentRoute: String = ""
            set(value) {
                Logger.verbose("PingNavState.currentRoute = $value")
                field = value
            }
    }
}

@Composable
fun PingAppContainer(
    navController: NavHostController,
    startDestination: String,
    userViewModel: UserViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
    signInViewModel: SignInViewModel = hiltViewModel(),
) {
    var settingsStartDestination: SettingsNav? = null
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable(navSignIn) {
            PingNavState.currentRoute = navSignIn
            signInViewModel.resetUiState()
            FrxContainer(
                signInViewModel = signInViewModel,
                onUserSignedIn = {
                    userViewModel.refreshUserList()
                    homeViewModel.startListeningToChats()
                    signInViewModel.viewModelScope.launch {
                        navController.navigate(navHome)
                    }
                }
            )
        }

        composable(navHome) {
            PingNavState.currentRoute = navHome
            HomeScreenContainer(
                homeViewModel = homeViewModel,
                userViewModel = userViewModel,
                onUserClick = {
                    navController.navigate("$navChat/${it.docId}")
                },
                onChatClick = {
                    navController.navigate("$navChat/${it.otherGuy.docId}")
                },
                onNavigateSettings = {
                    settingsStartDestination = it
                    navController.navigate(navSettings)
                }
            )
        }

        composable("$navChat/{userId}") {
            PingNavState.currentRoute = navChat
            val userId = it.arguments?.getString("userId")
            LaunchedEffect(key1 = userId) {
                if (userId != null) chatViewModel.setUser(userId)
                else Logger.error("userId is null")
            }
            ChatScreenContainer(
                chatViewModel = chatViewModel,
                onBackClick = {
                    chatViewModel.onScreenExit()
                    navController.navigate(navHome)
                }
            )
        }

        composable(navSettings) {
            PingNavState.currentRoute = navSettings
            SettingScreenContainer(
                startDestination = settingsStartDestination!!,
                onLoggedOut = {
                    navController.navigate(navSignIn)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}