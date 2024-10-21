package com.joshgm3z.ping.ui.screens.frx

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.Green40
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.WelcomeViewModel

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
    onUserSyncComplete: () -> Unit = {},
) {
    viewModel?.downloadUserList(onUserSyncComplete)
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 30.dp,
                horizontal = 20.dp
            ),
    ) {
        Column {
            Text(
                text = "Hello $name,",
                color = Green40,
                fontSize = 55.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Welcome to ping!",
                color = colorScheme.primary,
                fontSize = 50.sp,
                lineHeight = 60.sp,
            )
            Spacer(Modifier.height(60.dp))
            Text(
                text = "Please wait",
                color = colorScheme.primary,
                fontSize = 30.sp,
            )
            Spacer(Modifier.height(30.dp))
            CircularProgressIndicator()
        }
    }
}