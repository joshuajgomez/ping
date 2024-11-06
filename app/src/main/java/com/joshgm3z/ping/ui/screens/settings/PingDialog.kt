package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
fun PreviewPingDialog() {
    PingTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PingDialog()
        }
    }
}

@Composable
fun PingDialog(
    title: String = "Clear chats",
    message: String = "Chats with user cleared",
    onButtonClick: () -> Unit = {},
) {
    ElevatedCard {
        Column(
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = 10.dp,
                start = 20.dp,
                end = 20.dp
            )
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(Modifier.size(20.dp))
            Text(message)
            Spacer(Modifier.size(20.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onButtonClick) {
                    Text("OK", color = colorScheme.primary)
                }
            }
        }
    }
}