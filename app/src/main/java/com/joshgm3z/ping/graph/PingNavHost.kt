package com.joshgm3z.ping.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.joshgm3z.ping.ui.common.LoadingContainer
import com.joshgm3z.ping.ui.screens.home.HomeScreenContainer
import com.joshgm3z.ping.ui.screens.search.AllSearchContainer
import com.joshgm3z.ping.ui.viewmodels.EditType
import kotlinx.serialization.Serializable

@Serializable
data object Parent

@Serializable
data object Frx

@Serializable
data object SignIn

@Serializable
data class Welcome(val name: String)

@Serializable
data class Loading(val message: String)

@Serializable
data object Home

fun NavController.navigateToHome() = navigate(Home)

@Serializable
data object AllSearch

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
data class EditScreen(val type: EditType)

@Serializable
data object SignOut

@Serializable
data object GoodBye

fun NavController.goBack() = popBackStack()

@Composable
fun PingNavHost(startRoute: Any) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startRoute,
        route = Parent::class
    ) {
        frxGraph(navController)
        loadingGraph(navController)
        homeGraph(navController)
        searchGraph(navController)
        chatGraph(navController)
        settingGraph(navController)
        dialogGraph(navController)
    }
}

fun NavGraphBuilder.searchGraph(navController: NavHostController) {
    composable<AllSearch> {
        AllSearchContainer(onCloseClick = navController::goBack)
    }
}

fun NavGraphBuilder.loadingGraph(navController: NavHostController) {
    composable<Loading> {
        val message = it.toRoute<Loading>().message
        LoadingContainer(message)
    }
}

fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    composable<Home> {
        HomeScreenContainer(
            navController = navController,
            onSearchClick = { navController.navigate(AllSearch) }
        )
    }
}
