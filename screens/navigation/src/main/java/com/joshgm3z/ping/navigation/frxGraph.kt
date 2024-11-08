package com.joshgm3z.ping.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.joshgm3z.common.navigation.Frx
import com.joshgm3z.common.navigation.GoodBye
import com.joshgm3z.common.navigation.SignIn
import com.joshgm3z.common.navigation.Welcome
import com.joshgm3z.common.navigation.navigateToHome
import com.joshgm3z.settings.GoodByeScreen

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
