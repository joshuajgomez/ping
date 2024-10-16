package com.joshgm3z.ping.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.joshgm3z.ping.ui.screens.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.screens.chat.ImagePreview
import com.joshgm3z.ping.ui.screens.home.HomeScreenContainer
import kotlinx.serialization.Serializable

@Serializable
data object Parent

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
data class ChatImagePreview(val imageUrl: String, val name: String)

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
        startDestination = startRoute,
        route = Parent::class
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
        ChatScreenContainer(navController = navController)
    }
    composable<ChatImagePreview> {
        val imageUrl: String = it.toRoute<ChatImagePreview>().imageUrl
        val name: String = it.toRoute<ChatImagePreview>().name
        ImagePreview(
            navController,
            name = name,
            imageUrl = imageUrl
        )
    }
}







