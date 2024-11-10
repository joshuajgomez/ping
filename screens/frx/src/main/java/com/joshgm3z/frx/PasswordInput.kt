package com.joshgm3z.frx

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
fun PasswordInput(modifier: Modifier = Modifier) {
    PingTheme {
        var hidePassword by remember { mutableStateOf(true) }
        var password by remember { mutableStateOf("something@234") }
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = modifier,
            trailingIcon = {
                IconButton(onClick = {
                    hidePassword = !hidePassword
                }) {
                    Icon(
                        imageVector = if (hidePassword) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = "show password"
                    )
                }
            },
            visualTransformation = if (hidePassword) PasswordVisualTransformation()
            else VisualTransformation.None,
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
        )
    }
}
