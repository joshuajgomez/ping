package com.joshgm3z.ping.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.joshgm3z.common.PdfViewer
import com.joshgm3z.common.navigation.PdfViewerRoute
import com.joshgm3z.ping.ui.screens.chat.ChatImageViewer
import com.joshgm3z.ping.ui.screens.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.screens.chat.ImageViewer
import com.joshgm3z.ping.ui.screens.chat.navigateToImageViewer
import com.joshgm3z.ping.ui.screens.settings.navigateToUserInfo

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
    composable<ChatImageViewer> {
        ImageViewer(onBackClick = navController::goBack)
    }
    composable<PdfViewerRoute> {
        PdfViewer(
            onBackClick = navController::goBack
        )
    }
}