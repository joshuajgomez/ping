package com.joshgm3z.ping

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.joshgm3z.ping.service.PingService
import com.joshgm3z.ping.viewmodels.ChatViewModel
import com.joshgm3z.ping.ui.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.frx.SignInContainer
import com.joshgm3z.ping.viewmodels.SignInViewModel
import com.joshgm3z.ping.ui.home.HomeScreenContainer
import com.joshgm3z.ping.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.search.SearchContainer
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.utils.Logger
import com.joshgm3z.ping.utils.DataStoreUtil
import org.koin.android.ext.android.get

class HomeActivity : ComponentActivity() {

    private val navSignIn = "signin_screen"
    private val navHome = "home_screen"
    private val navSearch = "search_screen"
    private val navChat = "chat_screen"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAnalytics.getInstance(this).logEvent("MainActivity_onCreate", Bundle())
        if (!PingService.isRunning) {
            Logger.debug("PingService.isRunning = [${PingService.isRunning}]")
            val intent = Intent(this, PingService.javaClass)
            startService(intent)
        }
        setContent {
            PingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val dataStore: DataStoreUtil = get()
                    val isUserSignedIn = dataStore.isUserSignedIn()

                    NavHost(
                        navController = navController,
                        startDestination = if (isUserSignedIn) navHome else navSignIn
                    ) {

                        composable(navSignIn) {
                            Logger.warn("signIn")
                            SignInContainer(
                                signInViewModel = get(),
                                goToHome = { navController.navigate(navHome) }
                            )
                        }

                        composable(navHome) {
                            Logger.warn("navHome")
                            HomeScreenContainer(
                                homeViewModel = get(),
                                onSearchClick = { navController.navigate(navSearch) }
                            )
                        }

                        val chatViewModel: ChatViewModel = get()
                        composable("$navChat/{userId}") {
                            val userId = it.arguments?.getString("userId")
                            LaunchedEffect(key1 = userId) {
                                Logger.warn("navChat setUser = [$userId]")
                                chatViewModel.setUser(userId!!)
                            }
                            Logger.warn("navChat userId = [$userId]")
                            ChatScreenContainer(
                                chatViewModel = chatViewModel,
                                onBackClick = { navController.navigate(navHome) }
                            )
                        }

                        composable(navSearch) {
                            Logger.warn("navSearch")
                            SearchContainer(
                                homeViewModel = get(),
                                onSearchItemClick = { navController.navigate("$navChat/${it.docId}") },
                                onCancelClick = { navController.navigate(navHome) }
                            )
                        }

                    }
                }
            }
        }
    }
}