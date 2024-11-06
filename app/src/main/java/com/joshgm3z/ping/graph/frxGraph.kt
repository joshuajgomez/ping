package com.joshgm3z.ping.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.joshgm3z.frx.FrxContainer
import com.joshgm3z.frx.WelcomeScreen
import com.joshgm3z.ping.ui.screens.settings.GoodByeScreen

fun NavGraphBuilder.frxGraph(navController: NavHostController) {
    navigation<Frx>(startDestination = SignIn) {
        composable<SignIn> {
            com.joshgm3z.frx.FrxContainer(onSignInComplete = {
                navController.navigate(Welcome(it))
            })
        }
        composable<Welcome> {
            val name = it.toRoute<Welcome>().name
            com.joshgm3z.frx.WelcomeScreen(
                name,
                onButtonClick = navController::navigateToHome
            )
        }
        composable<GoodBye> {
            GoodByeScreen(navController)
        }
    }
}
