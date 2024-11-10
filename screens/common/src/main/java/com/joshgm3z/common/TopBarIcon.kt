package com.joshgm3z.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewTopBarIcon() {
    PingTheme {
        TopBarIcon()
    }
}

@Composable
fun TopBarIconAccent(
    icon: ImageVector = Icons.Default.Settings,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    TopBarIcon(
        icon,
        enabled,
        color = colorScheme.onPrimary,
        bgColor = colorScheme.primary,
        onClick = onClick
    )
}

@Composable
fun TopBarIcon(
    icon: ImageVector = Icons.Default.Settings,
    enabled: Boolean = true,
    color: Color = colorScheme.primary,
    bgColor: Color = colorScheme.onPrimary,
    onClick: () -> Unit = {},
) {
    IconButton(
        onClick,
        enabled = enabled,
        modifier = Modifier
            .background(
                bgColor,
                shape = CircleShape
            )
            .padding(0.dp),
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(25.dp),
            tint = when {
                enabled -> color
                else -> color.copy(alpha = 0.5f)
            }
        )
    }
}
