package com.joshgm3z.ping.ui.screens.frx

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.ping.graph.SignUp
import com.joshgm3z.ping.graph.Welcome
import com.joshgm3z.ping.ui.common.CustomTextField3
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.Green40
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.viewmodels.SignInViewModel
import com.joshgm3z.ping.ui.viewmodels.UserViewModel

@Preview
@Composable
fun PreviewFullSignInInput() {
    PingTheme {
        SignInInput()
    }
}

@Composable
fun SignInInput(
    navController: NavHostController = rememberNavController(),
    signInViewModel: SignInViewModel? = getIfNotPreview { hiltViewModel() },
    userViewModel: UserViewModel? = getIfNotPreview { hiltViewModel() },
    homeViewModel: HomeViewModel? = getIfNotPreview { hiltViewModel() },
    onSignInComplete: (name: String) -> Unit = {
        userViewModel?.refreshUserList()
        homeViewModel?.startListeningToChats()
        navController.navigate(Welcome(it))
    },
    goToSignUp: (name: String) -> Unit = {
        navController.navigate(SignUp(it))
    }
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Wallpaper()

        Column(
            modifier = Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = 70.dp
                )
        ) {

            Text(
                "Welcome to",
                color = colorScheme.onSurface,
                fontSize = 40.sp,
            )
            Text(
                "ping!",
                color = Green40,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 60.sp
            )

            Spacer(Modifier.height(100.dp))

            Text(
                text = "Sign in",
                color = colorScheme.onSurface,
                fontSize = 30.sp,
            )
            var name by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var error by remember { mutableStateOf("") }

            Spacer(Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        color = colorScheme.surfaceContainerHighest
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
            }

            Spacer(Modifier.height(10.dp))
            ErrorText(error)

            Spacer(Modifier.height(30.dp))
            var showLoading by remember { mutableStateOf(false) }
            SignInButton(
                showLoading,
                onClick = {
                    when {
                        name.isNotEmpty() -> {
                            error = ""
                            showLoading = true
                            signInViewModel?.onSignInClick(
                                name,
                                onSignInComplete = onSignInComplete,
                                onNewUser = {
                                    showLoading = false
                                    error = "User not found"
                                }
                            )
                        }

                        name.length <= 3 -> error = "Should be more that 3 letters"
                        else -> error = "Name cannot be empty"
                    }
                },
            )
            Spacer(Modifier.height(20.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = { goToSignUp(name) },
                ) {
                    Text(
                        text = "New user? Sign up",
                        textDecoration = TextDecoration.Underline,
                        fontSize = 16.sp,
                        color = Green40
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorText(error: String) {
    AnimatedVisibility(error.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.error, shape = RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                tint = colorScheme.onError
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = error,
                color = colorScheme.onError,
            )
        }
    }
}

@Composable
fun Wallpaper() {
    Box(modifier = Modifier.fillMaxSize()) {
        /*Image(
            painter = painterResource(R.drawable.wallpaper2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.6f))
                .fillMaxSize()
        ) {}*/
    }
}
