package com.joshgm3z.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewLoadingCard() {
    PingTheme {
        Box(Modifier.padding(20.dp)) {
            InfoCard()
        }
    }
}

@Composable
fun InfoCard(
    title: String = "Loading",
    subtitle: String = "Loading some more",
    icon: ImageVector = Icons.Default.ChatBubble
) {
    SmallCard {
        Icon(
            imageVector = icon,
            contentDescription = "empty message",
            modifier = Modifier.size(40.dp),
            tint = colorScheme.primary
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = title,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = subtitle,
            color = colorScheme.onSurface.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
    }
}
