package com.joshgm3z.ping

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.ping.service.PingService
import com.joshgm3z.ping.ui.PingAppContainer
import com.joshgm3z.ping.ui.navChat
import com.joshgm3z.ping.ui.navHome
import com.joshgm3z.ping.ui.navSignIn
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.utils.Logger
import com.joshgm3z.ping.utils.DataStoreUtil
import org.koin.android.ext.android.get

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!PingService.isRunning) {
            Logger.debug("PingService.isRunning = [${PingService.isRunning}]")
            startService(Intent(this, PingService::class.java))
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
                        userViewModel = get(),
                        homeViewModel = get(),
                        chatViewModel = get(),
                        signInViewModel = get(),
                    )
                    if (intent.hasExtra(OPEN_CHAT_USER)) {
                        navController.navigate(
                            "$navChat/${intent.getStringExtra(OPEN_CHAT_USER)}"
                        )
                        intent.removeExtra(OPEN_CHAT_USER)
                    }
                }
            }
        }
    }

    companion object {
        const val OPEN_CHAT_USER: String = "open_chat_user_id"
    }
}