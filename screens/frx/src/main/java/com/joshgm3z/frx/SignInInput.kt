package com.joshgm3z.frx

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.common.CustomTextField3
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.ErrorText
import com.joshgm3z.common.PingButton
import com.joshgm3z.common.PingButtonState
import com.joshgm3z.common.getIfNotPreview
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.frx.viewmodels.SignInViewModel

@DarkPreview
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
    disableControls: (Boolean) -> Unit = {},
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val showLoading: (Boolean) -> Unit = {
        isLoading = it
        disableControls(it)
    }

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
            "Sign in",
            state = PingButtonState.Accent(isLoading),
            onClick = onSignInClick
        )
        Spacer(Modifier.size(10.dp))
    }
}

