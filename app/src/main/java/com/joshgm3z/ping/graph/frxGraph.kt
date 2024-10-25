package com.joshgm3z.ping.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.joshgm3z.ping.ui.common.navigateToLoading
import com.joshgm3z.ping.ui.screens.frx.NewUserInput
import com.joshgm3z.ping.ui.screens.frx.SignInInput
import com.joshgm3z.ping.ui.screens.frx.WelcomeScreen
import com.joshgm3z.ping.ui.screens.settings.GoodByeScreen

fun NavGraphBuilder.frxGraph(navController: NavHostController) {
    navigation<Frx>(startDestination = SignIn) {
        composable<SignIn> {
            SignInInput(navController)
        }
        composable<SignUp>(enterTransition = slideIn) { it ->
            val name = it.toRoute<SignUp>().name
            NewUserInput(
                name,
                goToSignIn = {
                    navController.graph.clear()
                    navController.navigate(Frx)
                },
                onSignUpComplete = {
                    navController.navigate(Welcome(it))
                },
                showLoading = {
                    when {
                        it -> navController.navigateToLoading("Signing up")
                        else -> navController.goBack()
                    }
                })
        }
        composable<Welcome> {
            val name = it.toRoute<Welcome>().name
            WelcomeScreen(
                name,
                onUserSyncComplete = navController::navigateToHome
            )
        }
        composable<GoodBye> {
            GoodByeScreen(navController)
        }
    }
}
