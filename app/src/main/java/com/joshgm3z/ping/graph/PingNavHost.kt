package com.joshgm3z.ping.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.joshgm3z.ping.ui.screens.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.screens.home.HomeScreenContainer
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel
import kotlinx.serialization.Serializable

@Serializable
data object Frx

@Serializable
data object SignIn

@Serializable
data class SignUp(val name: String)

@Serializable
data class Welcome(val name: String)

@Serializable
data class Loading(val message: String)

@Serializable
data object Home

@Serializable
data class ChatScreen(val userId: String)

@Serializable
data class PingDialog(val title: String, val message: String)

@Serializable
data object SettingRoute

@Serializable
data object Profile

@Serializable
data object ImagePicker

@Serializable
data object Chat

@Serializable
data object Notifications

@Serializable
data class UserInfo(val userId: String)

@Serializable
data object Credits

@Serializable
data object SignOut

@Serializable
data object GoodBye

@Composable
fun PingNavHost(startRoute: Any) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        frxGraph(navController)
        composable<Loading> { }
        homeGraph(navController)
        chatGraph(navController)
        settingGraph(navController)
        dialogGraph(navController)
    }
}

fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    composable<Home> {
        HomeScreenContainer(navController = navController)
    }
}

fun NavGraphBuilder.chatGraph(navController: NavHostController) {
    composable<ChatScreen> {
        val userId: String = it.toRoute<ChatScreen>().userId
        val chatViewModel: ChatViewModel = hiltViewModel()
        LaunchedEffect(key1 = userId) {
            chatViewModel.setUser(userId)
        }
        ChatScreenContainer(navController = navController)
    }
}







