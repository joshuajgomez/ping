package com.joshgm3z.ping.ui.screens.chat

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.getCameraLauncher
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.Green40
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.utils.FileUtil

@Composable
fun InputBox(
    openPreview: (Uri) -> Unit = {},
    onSendClick: (text: String) -> Unit = {},
) {
    var text by remember { mutableStateOf("") }
    val topRadius = 20.dp
    Row(
        Modifier
            .clip(RoundedCornerShape(topStart = topRadius, topEnd = topRadius))
            .fillMaxWidth()
            .background(colorScheme.surface)
            .padding(10.dp)
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
                    tint = Green40
                )
            }
            Spacer(Modifier.size(5.dp))
            IconButton(
                onClick = { onSendClick(text) },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(25.dp)
                    .background(Green40)
                    .padding(3.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = null,
                )
            }
        }
    }
}

@DarkPreview
@Composable
fun PreviewInputBox2() {
    PingTheme {
        Box(Modifier.padding(10.dp)) {
            InputBox()
        }
    }
}

/*
@Composable
@Preview
fun PreviewInputMessage() {
    PingTheme {
        InputBox(defaultText = "Hello ")
    }
}

@Composable
@Preview
fun PreviewInputEmpty() {
    PingTheme {
        InputBox(defaultText = "")
    }
}

@Composable
@Preview
fun PreviewVeryLongInput() {
    PingTheme {
        InputBox(defaultText = "Hello Hello Hello Hello Hello Hello Hello Hello Hello ")
    }
}*/
