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
import com.joshgm3z.ping.viewmodels.ChatViewModel
import com.joshgm3z.ping.ui.chat.ChatScreen
import com.joshgm3z.ping.ui.chat.ChatScreenContainer
import com.joshgm3z.ping.ui.frx.SignInContainer
import com.joshgm3z.ping.viewmodels.SignInViewModel
import com.joshgm3z.ping.ui.home.HomeScreenContainer
import com.joshgm3z.ping.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.search.SearchContainer
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.utils.Logger
import com.joshgm3z.ping.utils.SharedPrefUtil
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : ComponentActivity() {

    private val navSignIn = "signin_screen"
    private val navHome = "home_screen"
    private val navSearch = "search_screen"
    private val navChat = "chat_screen"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAnalytics.getInstance(this).logEvent("MainActivity_onCreate", Bundle())
        setContent {
            PingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val chatViewModel by inject<ChatViewModel>()

                    NavHost(
                        navController = navController,
                        startDestination = if (SharedPrefUtil.isUserSignedIn()) navHome else navSignIn
                    ) {

                        composable(navSignIn) {
                            val signInViewModel by viewModel<SignInViewModel>()
                            SignInContainer(signInViewModel) {
                                // on sign in complete
                                navController.navigate(navHome)
                            }
                        }

                        composable(navHome) {
                            val homeViewModel by viewModel<HomeViewModel>()
                            HomeScreenContainer(homeViewModel) {
                                // on search icon click
                                navController.navigate(navSearch)
                            }
                        }

                        composable("$navChat/{userId}") {
                            val userId = it.arguments?.getString("userId")
                            Logger.warn("navChat userId = [$userId]")
                            chatViewModel.fetchUser(userId!!)
                            ChatScreenContainer(
                                chatViewModel = chatViewModel,
                                onBackClick = { navController.navigate(navHome) }
                            )
                        }

                        composable(navSearch) {
                            val homeViewModel by viewModel<HomeViewModel>()
                            SearchContainer(
                                homeViewModel = homeViewModel,
                                onSearchItemClick = {
                                    // launch chat screen for selected user
                                    navController.navigate("$navChat/${it.docId}")
                                },
                                onCancelClick = {
                                    // launch chat screen for selected user
                                    navController.navigate(navHome)
                                }
                            )
                        }

                    }
                }
            }
        }
    }
}