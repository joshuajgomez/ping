package com.joshgm3z.ping.ui.frx

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.CustomTextField
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Yellow40

@Preview
@Composable
fun PreviewNewUserInput() {
    PingTheme {
        NewUserInput()
    }
}

@Composable
fun NewUserInput(
    inputName: String = "im new",
    onSignInClick: (name: String) -> Unit = {},
    onGoBackClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Sign up to ping!",
            color = colorScheme.onSurface,
            fontSize = 20.sp,
        )

        Spacer(modifier = Modifier.height(30.dp))

        ImageInput()

        Spacer(modifier = Modifier.height(30.dp))

        var name by remember { mutableStateOf(inputName) }
        var error by remember { mutableStateOf("") }

        CustomTextField(
            text = name,
            hintText = "enter a username",
            onTextChanged = {
                name = it
                error = ""
            },
            modifier = Modifier.padding(horizontal = 50.dp)
        )

        AnimatedVisibility(visible = error.isNotEmpty()) {
            ErrorText()
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (name.isNotEmpty())
                    onSignInClick(name)
                else if (name.length <= 3)
                    error = "should be more that 3 letters"
                else
                    error = "name cannot be empty"
            },
        ) {
            Text(text = "sign up", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))

        TextButton(onClick = { onGoBackClick() }) {
            Text(
                text = "sign in to another account",
                color = colorScheme.outline,
                textDecoration = TextDecoration.Underline
            )
        }
    }
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
