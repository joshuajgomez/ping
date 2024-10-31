package com.joshgm3z.ping.ui.screens.chat

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.twotone.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.util.randomChat
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.theme.PingTheme

sealed class InlinePreviewState {
    data object Empty : InlinePreviewState()
    data class Reply(val chat: Chat) : InlinePreviewState()
    data class Image(val imageUri: Uri) : InlinePreviewState()
    data class WebUrl(val url: String) : InlinePreviewState()
}

@Composable
fun InputBox(
    openCamera: () -> Unit = {},
    deletePreview: () -> Unit = {},
    onSendClick: (text: String) -> Unit = {},
    preview: InlinePreviewState = InlinePreviewState.Empty,
) {
    var text by remember { mutableStateOf("") }
    val topRadius = 20.dp
    Column(
        Modifier
            .clip(RoundedCornerShape(topStart = topRadius, topEnd = topRadius))
            .fillMaxWidth()
            .background(colorScheme.surface)
            .padding(10.dp)
    ) {
        AnimatedVisibility(preview !is InlinePreviewState.Empty) {
            InputPreview(
                state = preview,
                onDeleteClick = deletePreview
            )
        }
        MessageBox(
            text = text,
            onTextChange = { text = it },
            onSendClick = {
                text = ""
                onSendClick(it)
            },
            openCamera = openCamera
        )
    }
}

@Composable
private fun MessageBox(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: (String) -> Unit,
    openCamera: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.surfaceContainer, shape = RoundedCornerShape(30.dp))
            .padding(start = 15.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            textStyle = LocalTextStyle.current.copy(
                color = colorScheme.onSurface.copy(alpha = 0.8f),
            ),
            decorationBox = { innerTextField ->
                when {
                    text.isEmpty() -> Text(
                        "Type something",
                        color = colorScheme.onSurface.copy(alpha = 0.3f)
                    )

                    else -> innerTextField()
                }
            }

        )

        IconButton(openCamera) {
            Icon(
                Icons.TwoTone.CameraAlt,
                contentDescription = null,
                tint = colorScheme.primary
            )
        }
        Spacer(Modifier.size(5.dp))
        IconButton(
            onClick = { onSendClick(text) },
            modifier = Modifier
                .clip(CircleShape)
                .size(25.dp)
                .background(colorScheme.primary)
                .padding(3.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = null,
                tint = colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun InputPreview(
    state: InlinePreviewState,
    onDeleteClick: () -> Unit = {}
) {
    if (state == InlinePreviewState.Empty) return
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ) {
        Icon(
            Icons.Rounded.Close,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .background(color = colorScheme.surface)
                .padding(3.dp)
                .clickable { onDeleteClick() },
            tint = colorScheme.onSurface
        )
        Column(
            modifier = Modifier
                .padding(
                    bottom = 15.dp,
                    start = 10.dp, end = 10.dp,
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            when (state) {
                is InlinePreviewState.Reply -> {
                    Text(
                        state.chat.fromUserName.ifEmpty { "Unknown" },
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary
                    )
                    Text(
                        text = state.chat.message,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colorScheme.onSurface
                    )
                }

                is InlinePreviewState.Image -> {
                    Text(
                        "Send image",
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary
                    )
                    Spacer(Modifier.size(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = state.imageUri,
                            contentDescription = null,
                            error = painterResource(R.drawable.wallpaper2),
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(chatBubbleRadius)),
                            contentScale = ContentScale.Crop,
                        )
                        Spacer(Modifier.size(20.dp))
                        AddImage()
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun AddImage(onClick: () -> Unit = {}) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .clip(CircleShape)
            .background(colorScheme.primary)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = colorScheme.onPrimary,
            modifier = Modifier.size(60.dp)
        )
    }
}

@DarkPreview
@Composable
fun PreviewInputBox() {
    PingTheme {
        Box(Modifier.padding(10.dp)) {
            InputBox()
        }
    }
}

@DarkPreview
@Composable
fun PreviewInputBoxReply() {
    PingTheme {
        Box(Modifier.padding(10.dp)) {
            InputBox(preview = InlinePreviewState.Reply(randomChat()))
        }
    }
}

@DarkPreview
@Composable
fun PreviewInputBoxImage() {
    PingTheme {
        Box(Modifier.padding(10.dp)) {
            InputBox(preview = InlinePreviewState.Image(Uri.parse("")))
        }
    }
}
