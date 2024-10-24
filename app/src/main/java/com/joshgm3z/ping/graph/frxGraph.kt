package com.joshgm3z.ping.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.joshgm3z.ping.ui.screens.frx.NewUserInput
import com.joshgm3z.ping.ui.screens.frx.SignInInput
import com.joshgm3z.ping.ui.screens.frx.WelcomeScreen
import com.joshgm3z.ping.ui.screens.settings.GoodByeScreen

fun NavGraphBuilder.frxGraph(navController: NavHostController) {
    navigation<Frx>(startDestination = SignIn) {
        composable<SignIn> {
            SignInInput(navController)
        }
        composable<SignUp>(enterTransition = slideIn) {
            val name = it.toRoute<SignUp>().name
            NewUserInput(navController, name)
        }
        composable<Welcome>(enterTransition = slideIn) {
            val name = it.toRoute<Welcome>().name
            WelcomeScreen(
                name,
                onUserSyncComplete = navController::navigateToHome
            )
        }
        composable<GoodBye>(enterTransition = slideIn) {
            GoodByeScreen(navController)
        }
    }
}
