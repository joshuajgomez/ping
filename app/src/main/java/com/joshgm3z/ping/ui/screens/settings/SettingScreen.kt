package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel

@Composable
fun SettingScreenContainer(userViewModel: UserViewModel) {
    SettingScreen()
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    PingTheme {
        SettingScreen()
    }
}

@Composable
private fun SettingScreen(onSignOutClick: () -> Unit = {}) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (signOutBtn, image, name) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.default_user),
            contentDescription = "image",
            modifier = Modifier
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 30.dp)
                }
                .clip(CircleShape)
                .size(100.dp)
        )
        Text(
            text = "dude111",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.outline,
            modifier = Modifier.constrainAs(name) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(image.bottom, margin = 20.dp)
            })
        Button(
            onClick = { onSignOutClick() },
            modifier = Modifier
                .constrainAs(signOutBtn) {
                    bottom.linkTo(parent.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(text = "sign out")
        }
    }
}