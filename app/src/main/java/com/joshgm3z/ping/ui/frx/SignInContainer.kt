package com.joshgm3z.ping.ui.frx

import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Purple60
import com.joshgm3z.ping.ui.theme.Yellow40
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SignInContainer(signInViewModel: SignInViewModel, goToHome: () -> Unit) {
    val uiState = signInViewModel.uiState.collectAsState()
    when (uiState.value) {
        is SignInUiState.SignIn -> SignInScreen {
            signInViewModel.onSignInClick(it)
        }
        is SignInUiState.SignUp -> {}
        is SignInUiState.Loading -> LoadingContainer((uiState.value as SignInUiState.Loading).message)
        is SignInUiState.GoToHome -> goToHome()
    }
}

@Composable
fun SignInScreen(onSignInClick: (name: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Purple60),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_ping_foreground),
            contentDescription = "logo",
            modifier = Modifier
                .size(300.dp)
        )

        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Enter your name",
            color = Color.White,
            fontSize = 20.sp,
        )

        Spacer(modifier = Modifier.height(30.dp))

        var name by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }

        TextField(value = name, onValueChange = {
            name = it
            error = ""
        })

        AnimatedVisibility(visible = error.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = "error icon",
                    modifier = Modifier.size(22.dp),
                    tint = Yellow40
                )
                Text(
                    text = error,
                    fontSize = 17.sp,
                    color = Yellow40,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

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
            Text(text = "sign in to ping", fontSize = 20.sp)
        }
    }
}
