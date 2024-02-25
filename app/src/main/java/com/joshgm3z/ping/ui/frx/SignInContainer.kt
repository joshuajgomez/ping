package com.joshgm3z.ping.ui.frx

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.CustomTextField
import com.joshgm3z.ping.ui.common.LoadingContainer
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Yellow40
import com.joshgm3z.ping.viewmodels.SignInUiState
import com.joshgm3z.ping.viewmodels.SignInViewModel

@Composable
fun SignInContainer(signInViewModel: SignInViewModel, goToHome: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PingLogo()

        val uiState = signInViewModel.uiState.collectAsState()
        when (uiState.value) {
            is SignInUiState.SignIn -> SignInInput {
                signInViewModel.onSignInClick(it)
            }

            is SignInUiState.SignUp -> NewUserInput()
            is SignInUiState.Loading -> LoadingContainer((uiState.value as SignInUiState.Loading).message)
            is SignInUiState.GoToHome -> goToHome()
        }
    }
}

@Preview
@Composable
fun PreviewSignInInput() {
    PingTheme {
        SignInInput()
    }
}

@Preview
@Composable
fun PreviewNewUser() {
    PingTheme {
        NewUserInput()
    }
}

@Preview
@Composable
fun PreviewLoadingContainer() {
    PingTheme {
        LoadingContainer()
    }
}

@Preview
@Composable
fun PreviewErrorText() {
    PingTheme {
        ErrorText()
    }
}

@Preview
@Composable
fun PreviewSignInScreen() {
    PingTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PingLogo()
            SignInInput()
        }
    }
}

@Preview
@Composable
fun PreviewLoadingFull() {
    PingTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PingLogo()
            LoadingContainer()
        }
    }
}

@Preview
@Composable
fun PreviewNewUserFull() {
    PingTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PingLogo()
            NewUserInput()
        }
    }
}

@Composable
fun PingLogo() {
    LazyVerticalGrid(columns = GridCells.Adaptive(80.dp)) {
        items(count = 12) {
            Image(
                painter = painterResource(id = R.drawable.ping_logo),
                contentDescription = "logo",
                modifier = Modifier
            )
        }
    }
    Spacer(modifier = Modifier.height(80.dp))
}

@Composable
fun ErrorText(error: String = "Name cannot be empty") {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 5.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "error icon",
            modifier = Modifier.size(20.dp),
            tint = colorScheme.error
        )
        Text(
            text = error,
            fontSize = 17.sp,
            color = colorScheme.error,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}