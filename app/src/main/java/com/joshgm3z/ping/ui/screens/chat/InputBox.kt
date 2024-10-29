package com.joshgm3z.ping.ui.screens.chat

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.automirrored.twotone.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.sharp.CameraAlt
import androidx.compose.material.icons.twotone.CameraAlt
import androidx.compose.material.icons.twotone.Send
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.common.CustomTextField2
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.getCameraLauncher
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.Green40
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.utils.FileUtil

@Composable
fun InputBox(
    modifier: Modifier = Modifier,
    openPreview: (Uri) -> Unit = {},
    onSendClick: (text: String) -> Unit = {},
    defaultText: String = "",
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.surface)
    ) {

        val cameraUri = getIfNotPreview { FileUtil.getUri(LocalContext.current) }
        val cameraLauncher = getCameraLauncher {
            openPreview(cameraUri ?: Uri.parse(""))
        }

        Icon(
            imageVector = Icons.Outlined.CameraAlt,
            contentDescription = "open camera",
            tint = colorScheme.primary,
            modifier = Modifier
                .padding(3.dp)
                .size(40.dp)
                .padding(5.dp)
                .clickable { cameraLauncher.launch(cameraUri!!) }
        )
        var text by remember { mutableStateOf(defaultText) }
        CustomTextField2(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 5.dp),
            onTextChanged = {
                text = it
            },
            onEnterPressed = {
                onSendClick(text)
                text = ""
            }
        )
        SendButton(
            enabled = text.isNotEmpty(),
            icon = Icons.AutoMirrored.Filled.Send,
            onClick = {
                onSendClick(text)
                text = ""
            })
    }
}

@Composable
fun InputBox2(
    openPreview: (Uri) -> Unit = {},
    onSendClick: (text: String) -> Unit = {},
) {
    var text by remember { mutableStateOf("") }
    Row(
        Modifier
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
            IconButton({cameraLauncher.launch(cameraUri!!)}) {
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

@Composable
fun SendButton(
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    IconButton(
        enabled = enabled,
        onClick = {
            onClick()
        },
        modifier = Modifier.padding(end = 5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "send message",
            modifier = Modifier
                .clip(CircleShape)
                .size(35.dp)
                .background(color = colorScheme.onPrimary)
                .padding(all = 7.dp),
            tint = colorScheme.primary
        )
    }
}

@DarkPreview
@Composable
fun PreviewInputBox2() {
    PingTheme {
        InputBox2()
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
