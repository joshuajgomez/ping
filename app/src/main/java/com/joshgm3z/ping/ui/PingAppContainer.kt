package com.joshgm3z.ping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joshgm3z.ping.ui.PingNavState.Companion.navChat
import com.joshgm3z.ping.ui.PingNavState.Companion.navHome
import com.joshgm3z.ping.ui.PingNavState.Companion.navSearch
import com.joshgm3z.ping.ui.PingNavState.Companion.navSignIn
import com.joshgm3z.ping.ui.screens.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.screens.frx.FrxContainer
import com.joshgm3z.ping.ui.screens.home.HomeScreenContainer
import com.joshgm3z.ping.ui.screens.search.SearchContainer
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.viewmodels.SearchViewModel
import com.joshgm3z.ping.ui.viewmodels.SignInViewModel

const val navSignIn = "signin_screen"
const val navHome = "home_screen"
const val navSearch = "search_screen"
const val navChat = "chat_screen"

class PingNavState {
    companion object {
        const val navSignIn = "signin_screen"
        const val navHome = "home_screen"
        const val navSearch = "search_screen"
        const val navChat = "chat_screen"
        var currentRoute: String = ""
    }
}

@Composable
fun PingAppContainer(
    navController: NavHostController,
    startDestination: String,
    signInViewModel: SignInViewModel,
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
                signInViewModel = signInViewModel,
                goToHome = { navController.navigate(navHome) }
            )
        }

        composable(navHome) {
            PingNavState.currentRoute = navHome
            HomeScreenContainer(
                homeViewModel = homeViewModel,
                onSearchClick = { navController.navigate(navSearch) },
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

    }
}