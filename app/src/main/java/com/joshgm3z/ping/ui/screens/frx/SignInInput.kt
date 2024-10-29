package com.joshgm3z.ping.ui.screens.frx

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.CustomTextField3
import com.joshgm3z.ping.ui.common.ErrorText
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.SignInViewModel

@Preview
@Composable
fun PreviewFullSignInInput() {
    PingTheme {
        SignInContent()
    }
}

@Composable
fun SignInContent(
    signInViewModel: SignInViewModel? = getIfNotPreview { hiltViewModel() },
    onSignInComplete: (String) -> Unit = {},
    showLoading: (Boolean) -> Unit = {},
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val onSignInClick: () -> Unit = {
        when {
            name.isNotEmpty() -> {
                error = ""
                showLoading(true)
                signInViewModel?.onSignInClick(
                    name,
                    onSignInComplete = onSignInComplete,
                    onNewUser = {
                        showLoading(false)
                        error = "User not found"
                    },
                    onError = {
                        error = it
                    }
                )
            }

            name.length <= 3 -> error = "Should be more that 3 letters"
            else -> error = "Name cannot be empty"
        }
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = colorScheme.surfaceContainerHigh
            )
    ) {
        CustomTextField3(
            text = name,
            icon = Icons.Default.AlternateEmail,
            hintText = "enter your name",
            onTextChanged = {
                name = it
                error = ""
            })
        HorizontalDivider(thickness = 1.dp)
        CustomTextField3(
            text = password,
            icon = Icons.Default.Lock,
            hintText = "enter password",
            onTextChanged = {
                password = it
                error = ""
            })
        ErrorText(error)
        PingButton(
            "Create Account",
            onClick = onSignInClick
        )
    }
}

