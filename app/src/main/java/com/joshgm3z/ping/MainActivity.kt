package com.joshgm3z.ping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.analytics.FirebaseAnalytics
import com.joshgm3z.ping.ui.chat.ChatViewModel
import com.joshgm3z.ping.ui.chat.ui.ChatScreen
import com.joshgm3z.ping.ui.theme.PingTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
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
                    ChatScreen(
                        chatListLive = chatViewModel.chatList,
                        onSendClick = {chatViewModel.onSendButtonClick(it)},
                        user = chatViewModel.user
                        )
                }
            }
        }
    }
}