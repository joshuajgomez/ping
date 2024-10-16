package com.joshgm3z.ping.ui.screens.frx

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
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
import com.joshgm3z.ping.ui.common.CustomTextField
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
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Sign up to ping!",
            color = colorScheme.onSurface,
            fontSize = 20.sp,
        )


        ImageInput()

        var name by remember { mutableStateOf(inputName) }
        var imagePath by remember { mutableStateOf("") }

        var error by remember { mutableStateOf("") }

        CustomTextField(
            text = name,
            hintText = "enter a username",
            onTextChanged = {
                name = it
                error = ""
            },
            modifier = Modifier.padding(horizontal = 50.dp),
            isFocusNeeded = false
        )

        var about by remember { mutableStateOf(randomAbout()) }
        CustomTextField(
            text = about,
            hintText = "enter something about you",
            onTextChanged = {
                about = it
            },
            modifier = Modifier.padding(horizontal = 50.dp),
            isFocusNeeded = false
        )

        AnimatedVisibility(visible = error.isNotEmpty()) {
            ErrorText()
        }

        Button(
            onClick = {
                if (name.isNotEmpty())
                    signInViewModel?.onSignUpClick(name, imagePath, about) {
                        userViewModel?.refreshUserList()
                        homeViewModel?.startListeningToChats()
                        signInViewModel.viewModelScope.launch {
                            navController.navigate(Home)
                        }
                    }
                else if (name.length <= 3)
                    error = "should be more that 3 letters"
                else
                    error = "name cannot be empty"
            },
        ) {
            Text(text = "sign up", fontSize = 20.sp)
        }

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
