package com.joshgm3z.ping.ui.screens.chat

import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.util.randomChat
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.getCameraLauncher
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.utils.FileUtil

sealed class InputPreviewState {
    data object Empty : InputPreviewState()
    data class Reply(val chat: Chat) : InputPreviewState()
    data class Image(val imageUri: Uri) : InputPreviewState()
}

@Composable
fun InputBox(
    openPreview: (Uri) -> Unit = {},
    onSendClick: (text: String) -> Unit = {},
    preview: InputPreviewState = InputPreviewState.Empty,
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
        InputPreview(preview) {

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.surfaceContainer, shape = RoundedCornerShape(30.dp))
                .padding(start = 15.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
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
            val cameraUri = getIfNotPreview { FileUtil.getUri(LocalContext.current) }
            val cameraLauncher = getCameraLauncher {
                openPreview(cameraUri ?: Uri.parse(""))
            }
            IconButton({ cameraLauncher.launch(cameraUri!!) }) {
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
}

@Composable
fun InputPreview(
    state: InputPreviewState,
    onDeleteClick: () -> Unit
) {
    if (state == InputPreviewState.Empty) return
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
                is InputPreviewState.Reply -> {
                    Text(
                        "Reply to chat",
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

                is InputPreviewState.Image -> {
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
            InputBox(preview = InputPreviewState.Reply(randomChat()))
        }
    }
}

@DarkPreview
@Composable
fun PreviewInputBoxImage() {
    PingTheme {
        Box(Modifier.padding(10.dp)) {
            InputBox(preview = InputPreviewState.Image(Uri.parse("")))
        }
    }
}
