package com.joshgm3z.ping.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.dialog

fun NavGraphBuilder.dialogGraph(navController: NavHostController) {
    dialog<PingDialog> {}
}