package com.joshgm3z.ping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joshgm3z.ping.ui.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.frx.SignInContainer
import com.joshgm3z.ping.ui.home.HomeScreenContainer
import com.joshgm3z.ping.ui.search.SearchContainer
import com.joshgm3z.ping.viewmodels.ChatViewModel
import com.joshgm3z.ping.viewmodels.HomeViewModel
import com.joshgm3z.ping.viewmodels.SearchViewModel
import com.joshgm3z.ping.viewmodels.SignInViewModel

const val navSignIn = "signin_screen"
const val navHome = "home_screen"
const val navSearch = "search_screen"
const val navChat = "chat_screen"

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
            SignInContainer(
                signInViewModel = signInViewModel,
                goToHome = { navController.navigate(navHome) }
            )
        }

        composable(navHome) {
            HomeScreenContainer(
                homeViewModel = homeViewModel,
                onSearchClick = { navController.navigate(navSearch) },
                onChatClick = { navController.navigate("$navChat/${it.otherGuy.docId}") },
            )
        }

        composable("$navChat/{userId}") {
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
            SearchContainer(
                searchViewModel = searchViewModel,
                onSearchItemClick = { navController.navigate("$navChat/${it.docId}") },
                onCancelClick = { navController.navigate(navHome) }
            )
        }

    }
}