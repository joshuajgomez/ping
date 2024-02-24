package com.joshgm3z.ping

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.joshgm3z.ping.service.PingService
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.utils.Logger
import com.joshgm3z.ping.utils.DataStoreUtil
import org.koin.android.ext.android.get

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    val dataStore: DataStoreUtil = get()
                    val isUserSignedIn = dataStore.isUserSignedIn()
                    val navController = rememberNavController()
                    PingAppContainer(
                        navController = navController,
                        startDestination = if (isUserSignedIn) navHome else navSignIn,
                        signInViewModel = get(),
                        homeViewModel = get(),
                        chatViewModel = get(),
                        searchViewModel = get(),
                    )
                }
            }
        }
    }
}