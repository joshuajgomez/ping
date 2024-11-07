package com.joshgm3z.ping.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.theme.Gray60
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
fun PreviewAllSearchTextField() {
    PingTheme {
        AllSearchTextField()
    }
}

@Composable
fun AllSearchTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hintText: String = "Search anything",
    isFocusNeeded: Boolean = true,
    onTextChanged: (String) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    BasicTextField(
        value = text,
        textStyle = LocalTextStyle.current.copy(
            color = colorScheme.onSurface,
        ),
        onValueChange = onTextChanged,
        modifier = Modifier
            .background(
                color = Gray60,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .weight(1f)
                        .padding(start = 5.dp)
                ) {
                    if (text.isEmpty())
                        Text(
                            text = hintText,
                            style = LocalTextStyle.current.copy(
                                color = colorScheme.onSurface.copy(alpha = 0.3f),
                            )
                        )
                    innerTextField()
                }
                AnimatedVisibility(text.isNotEmpty()) {
                    ClearIcon { onTextChanged("") }
                }
            }
        }
    )
    LaunchedEffect(key1 = Unit) {
        if (isFocusNeeded) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
fun ClearIcon(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .background(
                colorScheme.onSurface.copy(alpha = 0.5f),
                shape = CircleShape
            )
            .size(16.dp)
            .padding(1.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = null,
            tint = colorScheme.surface
        )
    }
}
