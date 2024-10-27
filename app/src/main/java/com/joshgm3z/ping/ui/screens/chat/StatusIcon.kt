package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joshgm3z.data.model.Chat
import androidx.compose.material3.MaterialTheme.colorScheme

@Composable
fun StatusIcon(modifier: Modifier = Modifier, status: Long = Chat.READ) {
    Icon(
        imageVector = when (status) {
            Chat.SENT -> Icons.Default.Done
            Chat.DELIVERED -> Icons.Default.DoneAll
            Chat.READ -> Icons.Default.DoneAll
            else -> Icons.Default.AccessTime
        },
        contentDescription = "status",
        tint = when (status) {
            Chat.SAVED -> colorScheme.outlineVariant
            Chat.READ -> colorScheme.surfaceTint
            else -> colorScheme.onSurface
        },
        modifier = modifier.size(13.dp)
    )
}