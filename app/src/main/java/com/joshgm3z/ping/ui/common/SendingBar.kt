package com.joshgm3z.ping.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SendingBar(progress: Float) {
    Row(
        Modifier
            .padding(top = 6.dp)
            .background(
                Color.Black.copy(alpha = 0.5f),
                RoundedCornerShape(5.dp)
            )
            .padding(horizontal = 5.dp, vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val color = colorScheme.onSurface.copy(alpha = 0.8f)
        when (progress) {
            0f -> {
                CircularProgressIndicator(
                    color = color,
                    modifier = Modifier.size(15.dp)
                )
            }

            else -> CircularProgressIndicator(
                progress = { progress / 100 },
                color = color,
                modifier = Modifier.size(15.dp)
            )
        }
        Spacer(Modifier.size(5.dp))
        Text("Sending", color = color, fontSize = 13.sp)
    }
}
