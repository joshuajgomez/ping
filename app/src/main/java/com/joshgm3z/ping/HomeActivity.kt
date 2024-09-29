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
import com.joshgm3z.repository.PingRepository
import com.joshgm3z.ping.service.PingService
import com.joshgm3z.ping.ui.PingAppContainer
import com.joshgm3z.ping.ui.navChat
import com.joshgm3z.ping.ui.navHome
import com.joshgm3z.ping.ui.navSignIn
import com.joshgm3z.ping.ui.theme.PingTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var pingRepository: PingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!PingService.isRunning) {
            com.joshgm3z.utils.Logger.debug("PingService.isRunning = [${PingService.isRunning}]")
            startService(Intent(this, PingService::class.java))
        }
        setContent {
            PingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isUserSignedIn = pingRepository.isUserSignedIn()
                    val navController = rememberNavController()
                    PingAppContainer(
                        navController = navController,
                        startDestination = if (isUserSignedIn) navHome else navSignIn
                    )
                    if (intent.hasExtra(OPEN_CHAT_USER)) {
                        val startRoute = "$navChat/${intent.getStringExtra(OPEN_CHAT_USER)}"
                        com.joshgm3z.utils.Logger.debug("startRoute = [${startRoute}]")
                        navController.navigate(startRoute)
                        intent.removeExtra(OPEN_CHAT_USER)
                    }
                }
            }
        }
    }

    companion object {
        const val OPEN_CHAT_USER: String = "open_chat_user_id"
        const val REQUEST_IMAGE_CAPTURE = 12
    }
}