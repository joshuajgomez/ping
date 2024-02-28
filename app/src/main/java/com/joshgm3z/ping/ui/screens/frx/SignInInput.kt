package com.joshgm3z.ping.ui.screens.frx

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.common.CustomTextField
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Yellow40

@Preview
@Composable
fun PreviewFullSignInInput() {
    PingTheme {
        SignInInput()
    }
}

@Composable
fun SignInInput(onSignInClick: (name: String) -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        Text(
            text = "Welcome to ping!",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 20.sp,
        )

        var name by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }

        CustomTextField(
            text = name,
            hintText = "Enter your name",
            onTextChanged = {
                name = it
                error = ""
            },
            modifier = Modifier.padding(horizontal = 50.dp),
        )

        AnimatedVisibility(visible = error.isNotEmpty()) {
            ErrorText()
        }

        PasswordInput()

        Button(
            onClick = {
                if (name.isNotEmpty())
                    onSignInClick(name)
                else if (name.length <= 3)
                    error = "Should be more that 3 letters"
                else
                    error = "Name cannot be empty"
            },
        ) {
            Text(text = "sign in", fontSize = 20.sp)
        }
    }
}