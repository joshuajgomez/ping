package com.joshgm3z.common.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
data class PdfViewerRoute(
    val fileLocalUrl: String,
)

@Serializable
data object Parent

@Serializable
data object Frx

@Serializable
data object SignIn

@Serializable
data class Welcome(val name: String)

@Serializable
data class Loading(val message: String)

@Serializable
data object Home

fun NavController.navigateToHome() = navigate(Home) {
    popUpTo(graph.id) {
        inclusive = true
    }
}

@Serializable
data object AllSearch

@Serializable
data class ChatScreen(val userId: String, val chatId: String = "")

@Serializable
data class PingDialogRoute(val title: String, val message: String)

@Serializable
data object SettingRoute

@Serializable
data object SettingsHome

@Serializable
data object Profile

@Serializable
data object ImagePicker

@Serializable
data object ChatSettings

@Serializable
data object Notifications

@Serializable
data class UserInfoRoute(val userId: String)

@Serializable
data object Credits

@Serializable
data class EditScreenRoute(val type: String)

@Serializable
data object SignOut

@Serializable
data object GoodBye

@Serializable
data class ChatImageViewer(
    val chatId: String,
)