package com.joshgm3z.ping.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.theme.Gray60
import com.joshgm3z.ping.ui.theme.PingTheme

@Preview
@Composable
fun PreviewTextField() {
    PingTheme {
        CustomTextField()
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hintText: String = "Type something",
    isSingleLine: Boolean = true,
    isFocusNeeded: Boolean = true,
    onTextChanged: (text: String) -> Unit = {},
    onEnterPressed: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        modifier = modifier
            .fillMaxWidth()
            .height(57.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onEnterPressed()
                    true
                } else false
            },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(32.dp),
        placeholder = {
            Text(text = hintText, fontSize = 20.sp)
        },
        value = text,
        onValueChange = {
            if ("\n" !in it) onTextChanged(it)
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
        if (isFocusNeeded) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
fun CustomTextField3(
    modifier: Modifier = Modifier,
    text: String = "",
    hintText: String = "Type something",
    icon: ImageVector = Icons.Default.AlternateEmail,
    isSingleLine: Boolean = true,
    isFocusNeeded: Boolean = true,
    onTextChanged: (text: String) -> Unit = {},
    onEnterPressed: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        modifier = modifier
            .fillMaxWidth()
            .height(57.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onEnterPressed()
                    true
                } else false
            },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(32.dp),
        placeholder = {
            Text(text = hintText, fontSize = 20.sp)
        },
        value = text,
        onValueChange = {
            if ("\n" !in it) onTextChanged(it)
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        maxLines = if (isSingleLine) 1 else 5,
        singleLine = isSingleLine
    )
    LaunchedEffect(key1 = Unit) {
        if (isFocusNeeded) {
            focusRequester.requestFocus()
        }
    }
}

@Preview
@Composable
private fun PreviewCustomTextField2() {
    PingTheme {
        CustomTextField2(
            text = "",
        )
    }
}

@Preview
@Composable
private fun PreviewCustomTextField3() {
    PingTheme {
        CustomTextField3(
            text = "",
        )
    }
}

@Composable
fun CustomTextField2(
    modifier: Modifier = Modifier,
    text: String = "",
    hintText: String = "Type something",
    isSingleLine: Boolean = true,
    isFocusNeeded: Boolean = true,
    onTextChanged: (text: String) -> Unit = {},
    onEnterPressed: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    BasicTextField(modifier = modifier
        .background(
            Gray60,
            shapes.extraLarge,
        )
        .fillMaxWidth()
        .height(40.dp)
        .padding(horizontal = 10.dp)
        .focusRequester(focusRequester)
        .onKeyEvent {
            if (it.key == Key.Enter) {
                onEnterPressed()
                true
            } else false
        },
        value = text,
        onValueChange = {
            if ("\n" !in it) onTextChanged(it)
        },
        cursorBrush = SolidColor(colorScheme.primary),
        textStyle = LocalTextStyle.current.copy(
            color = colorScheme.onSurface,
            fontSize = 20.sp
        ),
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
                    if (text.isEmpty()) Text(
                        hintText,
                        style = LocalTextStyle.current.copy(
                            color = colorScheme.onSurface.copy(alpha = 0.3f),
                            fontSize = 20.sp
                        )
                    )
                    innerTextField()
                }
                if (text.isNotEmpty())
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = colorScheme.outlineVariant
                    )
            }
        }
    )
    LaunchedEffect(key1 = Unit) {
        if (isFocusNeeded) {
            focusRequester.requestFocus()
        }
    }
}