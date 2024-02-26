package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshgm3z.ping.ui.common.CustomTextField
import com.joshgm3z.ping.ui.theme.Gray40
import com.joshgm3z.ping.ui.theme.Gray50
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Purple10
import com.joshgm3z.ping.ui.theme.Purple60

@Composable
fun InputBox(
    modifier: Modifier = Modifier,
    onSendClick: (text: String) -> Unit = {},
    defaultText: String = "",
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.background)
    ) {
        Spacer(modifier = Modifier.width(5.dp))
        var text by remember { mutableStateOf(defaultText) }
        CustomTextField(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 5.dp, horizontal = 5.dp),
            isSingleLine = false,
            isFocusNeeded = false,
            onTextChanged = { text = it },
            onEnterPressed = {
                onSendClick(text)
                text = ""
            },
        )
        IconButton(
            enabled = text.isNotEmpty(),
            onClick = {
                onSendClick(text)
                text = ""
            },
            modifier = Modifier.padding(end = 5.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "send message",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(color = colorScheme.onSecondary)
                    .padding(all = 9.dp)
                    .clickable(enabled = text.isNotEmpty()) {
                        onSendClick(text)
                        text = ""
                    },
                tint = colorScheme.secondary
            )
        }
    }
}

@Composable
@Preview()
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
}