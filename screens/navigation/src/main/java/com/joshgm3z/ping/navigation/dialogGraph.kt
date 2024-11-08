package com.joshgm3z.ping.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.joshgm3z.common.navigation.PingDialogRoute
import com.joshgm3z.ping.ui.screens.settings.PingDialog

fun NavGraphBuilder.dialogGraph(navController: NavHostController) {
    dialog<PingDialogRoute> {
        val title = it.toRoute<PingDialogRoute>().title
        val message = it.toRoute<PingDialogRoute>().message
        PingDialog(
            title = title,
            message = message,
            onButtonClick = navController::goBack,
        )
    }
}