package com.joshgm3z.ping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.ping.service.PingService
import com.joshgm3z.ping.ui.PingAppContainer
import com.joshgm3z.ping.ui.navChat
import com.joshgm3z.ping.ui.navHome
import com.joshgm3z.ping.ui.navSignIn
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var currentUserInfo: CurrentUserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!PingService.isRunning) {
            PingService.start(this)
        }
        setContent {
            PingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    PingAppContainer(
                        navController = navController,
                        startDestination = if (currentUserInfo.isSignedIn) navHome else navSignIn
                    )
                    if (intent.hasExtra(OPEN_CHAT_USER)) {
                        val startRoute = "$navChat/${intent.getStringExtra(OPEN_CHAT_USER)}"
                        Logger.debug("startRoute = [${startRoute}]")
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