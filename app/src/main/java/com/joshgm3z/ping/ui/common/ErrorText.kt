package com.joshgm3z.ping.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorText(error: String) {
    AnimatedVisibility(error.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.error, shape = RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = colorScheme.onError
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = error,
                color = colorScheme.onError,
            )
        }
    }
}