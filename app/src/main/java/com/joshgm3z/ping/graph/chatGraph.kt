package com.joshgm3z.ping.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.joshgm3z.ping.ui.screens.chat.ChatImageViewer
import com.joshgm3z.ping.ui.screens.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.screens.chat.ImageViewer
import com.joshgm3z.ping.ui.screens.chat.navigateToImageViewer
import com.joshgm3z.ping.ui.screens.settings.navigateToUserInfo

fun NavGraphBuilder.chatGraph(navController: NavHostController) {
    composable<ChatScreen>(enterTransition = slideIn, exitTransition = slideOut) {
        ChatScreenContainer(
            goHome = navController::goBack,
            onUserInfoClick = navController::navigateToUserInfo,
            onImageClick = navController::navigateToImageViewer
        )
    }
    composable<ChatImageViewer> {
        ImageViewer(onBackClick = navController::goBack)
    }
}