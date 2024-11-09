package com.joshgm3z.common

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.State

@Composable
fun OnlineImage(
    model: Any?,
    error: Painter? = null,
    placeholder: Painter? = null,
    modifier: Modifier = Modifier.size(250.dp),
    onLoading: ((State.Loading) -> Unit)? = null,
    onSuccess: ((State.Success) -> Unit)? = null,
    onError: ((State.Error) -> Unit)? = null,
) {
    AsyncImage(
        model = model,
        placeholder = placeholder,
        error = error,
        contentDescription = null,
        modifier = modifier.clip(CircleShape),
        onLoading = onLoading,
        onSuccess = onSuccess,
        onError = onError,
    )
}