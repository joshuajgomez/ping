package com.joshgm3z.frx

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MarkChatUnread
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.LoadingContainer
import com.joshgm3z.common.PingButton
import com.joshgm3z.common.PingWallpaper
import com.joshgm3z.common.getIfNotPreview
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.frx.viewmodels.WelcomeViewModel

@DarkPreview
@Composable
fun PreviewWelcomeScreen() {
    PingTheme {
        WelcomeScreen()
    }
}

@Composable
fun WelcomeScreen(
    name: String = "Alien",
    viewModel: WelcomeViewModel? = getIfNotPreview { hiltViewModel() },
    onButtonClick: () -> Unit = {},
) {
    var showButton by remember { mutableStateOf(false) }
    viewModel?.downloadUserList {
        showButton = true
    }
    LoadingContainer(
        "Hello $name!",
        "Please wait for a sec",
        Icons.Outlined.MarkChatUnread
    )
    PingWallpaper {
        ElevatedCard(modifier = Modifier.padding(30.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                Text("Hello $name", fontWeight = FontWeight.Bold, color = colorScheme.primary)
                Spacer(Modifier.size(20.dp))
                Text(
                    when {
                        showButton -> "All set, press below button to start"
                        else -> "Please wait while we log you in"
                    }
                )
                Spacer(Modifier.size(30.dp))
                AnimatedVisibility(showButton) {
                    PingButton(text = "Lets go", onClick = onButtonClick)
                }
            }
        }
    }
}