package com.joshgm3z.common

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun OnlineImage(
    model: Any?,
    error: Painter? = null,
    placeholder: Painter? = null,
    modifier: Modifier = Modifier.size(250.dp),
    onLoading:()->Unit = {},
    onSuccess:()->Unit = {},
    onError:()->Unit = {}
) {
    OnlineImage(
        model = model,
        placeholder = placeholder,
        error = error,
        modifier = modifier.clip(CircleShape),
        onLoading = onLoading,
        onSuccess = onSuccess,
        onError = onError,
    )
}