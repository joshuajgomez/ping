package com.joshgm3z.ping.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.joshgm3z.common.LoadingContainer
import com.joshgm3z.common.navigation.Parent

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
        AllSearchContainer(
            onCloseClick = navController::goBack,
            onSearchResultClick = { userId, chatId ->
                navController.navigate(ChatScreen(userId, chatId))
            }
        )
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
            onSearchClick = { navController.navigate(com.joshgm3z.ping.navigation.AllSearch) }
        )
    }
}
