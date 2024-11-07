package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@Composable
fun ScrollButton(visible: Boolean = true, onClick: () -> Unit = {}) {
    AnimatedVisibility(visible) {
        Row(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = colorScheme.onPrimary)
                .clickable { onClick() }
                .padding(horizontal = 15.dp, vertical = 8.dp)
        ) {
            Icon(
                Icons.Default.ArrowDownward, contentDescription = null,
                tint = colorScheme.primary
            )
            Spacer(Modifier.size(10.dp))
            Text(
                text = "Scroll to bottom",
                color = colorScheme.primary
            )
        }
    }
}

@DarkPreview
@Composable
private fun PreviewScrollButton() {
    PingTheme {
        ScrollButton()
    }
}