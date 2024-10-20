package com.joshgm3z.ping.graph

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.joshgm3z.ping.ui.screens.chat.ChatImageViewer
import com.joshgm3z.ping.ui.screens.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.screens.chat.ImagePreview
import com.joshgm3z.ping.ui.screens.chat.ImageViewer
import com.joshgm3z.ping.ui.screens.chat.navigateToImageViewer
import com.joshgm3z.ping.ui.screens.settings.navigateToUserInfo

fun NavGraphBuilder.chatGraph(navController: NavHostController) {
    composable<ChatScreen>(enterTransition = slideIn, exitTransition = slideOut) {
        ChatScreenContainer(
            goHome = navController::navigateToHome,
            openPreview = navController::navigateToImagePreview,
            onUserInfoClick = navController::navigateToUserInfo,
            onImageClick = navController::navigateToImageViewer
        )
    }
    composable<ChatImagePreview>(enterTransition = slideIn) {
        ImagePreview(
            navController,
        )
    }
    composable<ChatImageViewer>(popEnterTransition = slideIn) {
        ImageViewer(onBackClick = navController::goBack)
    }
}