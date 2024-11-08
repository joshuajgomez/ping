package com.joshgm3z.ping.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.OnlineImage
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewChatImage() {
    PingTheme {
        ChatImage()
    }
}

@Composable
fun ChatImage(
    modifier: Modifier = Modifier,
    imageUrl: Any = "",
    placeHolderColor: Color = colorScheme.surfaceContainerHighest,
) {
    var maskImage by remember { mutableStateOf(true) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        OnlineImage(
            model = imageUrl,
            modifier = modifier.fillMaxSize(),
            onLoading = { maskImage = true },
            onSuccess = { maskImage = false },
            onError = { maskImage = false }
        )
        AnimatedVisibility(maskImage) {
            Box(
                Modifier
                    .background(color = placeHolderColor)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}