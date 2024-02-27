package com.joshgm3z.ping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joshgm3z.ping.ui.screens.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.screens.frx.FrxContainer
import com.joshgm3z.ping.ui.screens.home.HomeScreenContainer
import com.joshgm3z.ping.ui.screens.search.SearchContainer
import com.joshgm3z.ping.ui.screens.settings.SettingScreenContainer
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.viewmodels.SearchViewModel
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.ping.utils.Logger

const val navSignIn = "signin_screen"
const val navHome = "home_screen"
const val navSearch = "search_screen"
const val navChat = "chat_screen"
const val navSettings = "chat_settings"

class PingNavState {
    companion object {
        var currentRoute: String = ""
            set(value) {
                Logger.debug("PingNavState.currentRoute = $value")
                field = value
            }
    }
}

@Composable
fun PingAppContainer(
    navController: NavHostController,
    startDestination: String,
    userViewModel: UserViewModel,
    homeViewModel: HomeViewModel,
    chatViewModel: ChatViewModel,
    searchViewModel: SearchViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {

        composable(navSignIn) {
            PingNavState.currentRoute = navSignIn
            FrxContainer(
                signInViewModel = userViewModel,
                goToHome = {
                    homeViewModel.startListeningToChats()
                    navController.navigate(navHome)
                }
            )
        }

        composable(navHome) {
            PingNavState.currentRoute = navHome
            HomeScreenContainer(
                homeViewModel = homeViewModel,
                onSearchClick = { navController.navigate(navSearch) },
                onSettingsClick = { navController.navigate(navSettings) },
                onChatClick = { navController.navigate("$navChat/${it.otherGuy.docId}") },
            )
        }

        composable("$navChat/{userId}") {
            PingNavState.currentRoute = navChat
            val userId = it.arguments?.getString("userId")
            LaunchedEffect(key1 = userId) {
                chatViewModel.setUser(userId!!)
            }
            ChatScreenContainer(
                chatViewModel = chatViewModel,
                onBackClick = { navController.navigate(navHome) }
            )
        }

        composable(navSearch) {
            PingNavState.currentRoute = navSearch
            SearchContainer(
                searchViewModel = searchViewModel,
                onSearchItemClick = { navController.navigate("$navChat/${it.docId}") },
                onCancelClick = { navController.navigate(navHome) }
            )
        }

        composable(navSettings) {
            PingNavState.currentRoute = navSettings
            SettingScreenContainer(
                userViewModel = userViewModel,
                onGoBackClick = {
                    navController.navigate(navHome)
                }
            )
        }

    }
}