package com.joshgm3z.ping.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshgm3z.ping.ui.theme.Purple60

@Preview
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hintText: String = "Type something",
    isSingleLine: Boolean = true,
    onTextChanged: (text: String) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .focusRequester(focusRequester)
            .clip(RoundedCornerShape(20.dp)),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(32.dp),
        placeholder = {
            Text(text = hintText)
        },
        value = text,
        onValueChange = {
            onTextChanged(it)
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onTextChanged("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "clear",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        maxLines = if (isSingleLine) 1 else 5,
        singleLine = isSingleLine
    )
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
}