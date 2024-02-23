package com.joshgm3z.ping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.joshgm3z.ping.ui.chat.ChatViewModel
import com.joshgm3z.ping.ui.chat.ChatScreen
import com.joshgm3z.ping.ui.frx.SignInContainer
import com.joshgm3z.ping.ui.frx.SignInViewModel
import com.joshgm3z.ping.ui.home.HomeScreen
import com.joshgm3z.ping.ui.theme.PingTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAnalytics.getInstance(this).logEvent("MainActivity_onCreate", Bundle())
        setContent {
            PingTheme {
                val chatViewModel by viewModel<ChatViewModel>()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val signInViewModel by viewModel<SignInViewModel>()


                    NavHost(navController = navController, startDestination = "frx") {
                        composable("frx") {
                            SignInContainer(signInViewModel) {
                                navController.navigate("home")
                            }
                        }
                        composable("home") { HomeScreen() }
                        composable("chat_screen") {
                            ChatScreen(
                                chatListLive = chatViewModel.chatList,
                                onSendClick = { chatViewModel.onSendButtonClick(it) },
                                user = chatViewModel.user
                            )
                        }
                        // Add more destinations similarly.
                    }
                }
            }
        }
    }
}