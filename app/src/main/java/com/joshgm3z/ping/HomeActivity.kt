package com.joshgm3z.ping

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.joshgm3z.ping.service.PingService
import com.joshgm3z.ping.navigation.PingNavHost
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
            PingNavHost(
                getOpenChatUserId(intent),
                currentUserInfo.isSignedIn
            )
        }
    }

    private fun getOpenChatUserId(intent: Intent) = when {
        intent.hasExtra(OPEN_CHAT_USER) -> with(intent) {
            val userId = getStringExtra(OPEN_CHAT_USER)
            removeExtra(OPEN_CHAT_USER)
            userId
        }

        else -> null
    }

}
