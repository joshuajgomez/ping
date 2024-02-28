package com.joshgm3z.ping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joshgm3z.ping.ui.screens.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.screens.frx.FrxContainer
import com.joshgm3z.ping.ui.screens.home.HomeScreenContainer
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.viewmodels.SignInViewModel
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.ping.utils.Logger

const val navSignIn = "signin_screen"
const val navHome = "home_screen"
const val navChat = "chat_screen"

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
    signInViewModel: SignInViewModel,
) {
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
                    navController.navigate(navHome)
                }
            )
        }

        composable(navHome) {
            PingNavState.currentRoute = navHome
            HomeScreenContainer(
                homeViewModel = homeViewModel,
                userViewModel = userViewModel,
                signInViewModel = signInViewModel,
                onUserClick = {
                    navController.navigate("$navChat/${it.docId}")
                },
                onChatClick = {
                    navController.navigate("$navChat/${it.otherGuy.docId}")
                },
                onLoggedOut = {
                    navController.navigate(navSignIn)
                }
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
                onBackClick = {
                    navController.navigate(navHome)
                }
            )
        }
    }
}