package com.joshgm3z.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewTopBarIcon() {
    PingTheme {
        TopBarIcon()
    }
}

@Composable
fun TopBarIcon(
    icon: ImageVector = Icons.Default.Settings,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    IconButton(
        onClick,
        enabled = enabled,
        modifier = Modifier
            .background(
                colorScheme.onPrimary,
                shape = CircleShape
            )
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier
                .size(25.dp),
            tint = when {
                enabled -> colorScheme.primary
                else -> colorScheme.primary.copy(alpha = 0.5f)
            }
        )
    }
}
