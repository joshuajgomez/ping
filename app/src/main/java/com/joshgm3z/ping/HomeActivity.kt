package com.joshgm3z.ping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.joshgm3z.ping.service.PingService
import com.joshgm3z.ping.ui.PingAppContainer
import com.joshgm3z.ping.ui.TopLevelRoute
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.utils.const.OPEN_CHAT_USER
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
                    val startRoute = when {
                        intent.hasExtra(OPEN_CHAT_USER) -> with(intent) {
                            val userId = getStringExtra(OPEN_CHAT_USER)!!
                            removeExtra(OPEN_CHAT_USER)
                            TopLevelRoute.Chat(userId)
                        }

                        currentUserInfo.isSignedIn -> TopLevelRoute.Home

                        else -> TopLevelRoute.Frx
                    }
                    PingAppContainer(startRoute)
                }
            }
        }
    }

}