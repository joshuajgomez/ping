package com.joshgm3z.ping.ui.screens.frx

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.data.util.randomAbout
import com.joshgm3z.ping.R
import com.joshgm3z.ping.graph.Frx
import com.joshgm3z.ping.graph.Home
import com.joshgm3z.ping.graph.Welcome
import com.joshgm3z.ping.ui.common.CustomTextField
import com.joshgm3z.ping.ui.common.CustomTextField3
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.viewmodels.SignInViewModel
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@Preview
@Composable
fun PreviewNewUserInput() {
    PingTheme {
        NewUserInput()
    }
}

@Composable
fun NewUserInput(
    navController: NavController = rememberNavController(),
    inputName: String = "",
    signInViewModel: SignInViewModel? = getIfNotPreview { hiltViewModel() },
    userViewModel: UserViewModel? = getIfNotPreview { hiltViewModel() },
    homeViewModel: HomeViewModel? = getIfNotPreview { hiltViewModel() },
    onSignUpClick: (name: String, imagePath: String, about: String) -> Unit = { name, imagePath, about ->
        signInViewModel?.onSignUpClick(name, imagePath, about) {
            userViewModel?.refreshUserList()
            homeViewModel?.startListeningToChats()
            signInViewModel.viewModelScope.launch {
                navController.navigate(Welcome(it))
            }
        }
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
                fontStyle = FontStyle.Italic
            )
            Text(
                "ping!",
                color = colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 60.sp
            )

            Spacer(Modifier.height(100.dp))

            Text(
                text = "Sign up",
                color = colorScheme.onSurface,
                fontSize = 30.sp,
            )
            var name by remember { mutableStateOf(inputName) }
            var imagePath by remember { mutableStateOf("") }
            var about by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }
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
                    icon = Icons.Default.LockOpen,
                    hintText = "enter password",
                    onTextChanged = {
                        password = it
                        error = ""
                    })
                HorizontalDivider(thickness = 1.dp)
                CustomTextField3(
                    text = confirmPassword,
                    icon = Icons.Default.Lock,
                    hintText = "confirm password",
                    onTextChanged = {
                        confirmPassword = it
                        error = ""
                    })
                HorizontalDivider(thickness = 1.dp)
                CustomTextField3(
                    text = about,
                    icon = Icons.Default.EditNote,
                    hintText = "something about you",
                    onTextChanged = {
                        about = it
                        error = ""
                    })
            }

            Spacer(Modifier.height(10.dp))
            ErrorText(error)

            Spacer(Modifier.height(30.dp))
            var showLoading by remember { mutableStateOf(false) }
            SignInButton(
                showLoading,
                text = if (showLoading) "Signing up" else "Sign up",
                onClick = {
                    when {
                        name.isNotEmpty() -> {
                            showLoading = true
                            onSignUpClick(name, imagePath, about)
                        }

                        name.length <= 3 -> error = "Should be more that 3 letters"
                        else -> error = "Name cannot be empty"
                    }
                },
            )

            Spacer(Modifier.height(20.dp))

            TextButton(onClick = {
                navController.navigate(Frx)
            }) {
                Text(
                    text = "sign in to another account",
                    color = colorScheme.outline,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Composable
fun ErrorText() {
    Text("error", color = colorScheme.error)
}

@Composable
fun ImageInput() {
    ConstraintLayout {
        val (image, icon) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.default_user_image),
            contentDescription = "profile pic",
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(100.dp)

        )
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "edit image",
            tint = colorScheme.surfaceTint,
            modifier = Modifier.constrainAs(image) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}
