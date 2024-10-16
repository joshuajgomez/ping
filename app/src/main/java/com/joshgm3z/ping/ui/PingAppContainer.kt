package com.joshgm3z.ping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.joshgm3z.data.model.User
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
import kotlinx.serialization.Serializable

/*const val navSignIn = "signin_screen"
const val navHome = "home_screen"
const val navChat = "chat_screen"
const val navSettings = "navSettings"*/

@Serializable
sealed class TopLevelRoute {
    @Serializable
    data object Frx : TopLevelRoute()
    @Serializable
    data object Home : TopLevelRoute()
    @Serializable
    data class Chat(val userId: String) : TopLevelRoute()
    @Serializable
    data object Settings : TopLevelRoute()
}

class PingNavState {
    companion object {
        var currentRoute: TopLevelRoute? = null
            set(value) {
                Logger.verbose("PingNavState.currentRoute = $value")
                field = value
            }
    }
}

@Composable
fun PingAppContainer(
    startDestination: TopLevelRoute,
    userViewModel: UserViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
    signInViewModel: SignInViewModel = hiltViewModel(),
) {
    var settingsStartDestination: SettingsNav? = null
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable<TopLevelRoute.Frx> {
            PingNavState.currentRoute = it.toRoute()
            signInViewModel.resetUiState()
            FrxContainer(
                signInViewModel = signInViewModel,
                onUserSignedIn = {
                    userViewModel.refreshUserList()
                    homeViewModel.startListeningToChats()
                    signInViewModel.viewModelScope.launch {
                        navController.navigate(TopLevelRoute.Home)
                    }
                }
            )
        }

        composable<TopLevelRoute.Home> {
            PingNavState.currentRoute = it.toRoute()
            HomeScreenContainer(
                homeViewModel = homeViewModel,
                userViewModel = userViewModel,
                onUserClick = {
                    navController.navigate(TopLevelRoute.Chat(it.docId))
                },
                onChatClick = {
                    navController.navigate(TopLevelRoute.Chat(it.otherGuy.docId))
                },
                onNavigateSettings = {
                    settingsStartDestination = it
                    navController.navigate(TopLevelRoute.Settings)
                }
            )
        }

        composable<TopLevelRoute.Chat> {
            PingNavState.currentRoute = it.toRoute()
            val userId: String = it.toRoute<TopLevelRoute.Chat>().userId
            LaunchedEffect(key1 = userId) {
                chatViewModel.setUser(userId)
            }
            ChatScreenContainer(
                chatViewModel = chatViewModel,
                onBackClick = {
                    chatViewModel.onScreenExit()
                    navController.navigate(TopLevelRoute.Home)
                },
                onUserInfoClick = {
                    settingsStartDestination = SettingsNav.UserInfo(it.docId)
                    navController.navigate(TopLevelRoute.Settings)
                }
            )
        }

        composable<TopLevelRoute.Settings> {
            PingNavState.currentRoute = it.toRoute()
            SettingScreenContainer(
                startDestination = settingsStartDestination!!,
                onLoggedOut = {
                    navController.navigate(TopLevelRoute.Settings)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
