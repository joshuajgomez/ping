package com.joshgm3z.ping.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.joshgm3z.common.PdfViewer
import com.joshgm3z.common.navigation.PdfViewerRoute
import com.joshgm3z.settings.navigateToUserInfo

fun NavGraphBuilder.chatGraph(navController: NavHostController) {
    composable<ChatScreen>(enterTransition = slideIn, exitTransition = slideOut) { it ->
        ChatScreenContainer(
            goHome = navController::goBack,
            onUserInfoClick = navController::navigateToUserInfo,
            onImageClick = navController::navigateToImageViewer,
            onPdfClick = { navController.navigate(PdfViewerRoute(it)) },
            scrollToChatId = it.toRoute<ChatScreen>().chatId,
        )
    }
    composable<com.joshgm3z.ping.chat.ChatImageViewer> {
        com.joshgm3z.ping.chat.ImageViewer(onBackClick = navController::goBack)
    }
    composable<PdfViewerRoute> {
        PdfViewer(
            onBackClick = navController::goBack
        )
    }
}