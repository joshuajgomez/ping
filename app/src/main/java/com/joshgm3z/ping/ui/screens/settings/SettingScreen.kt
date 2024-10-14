package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun SettingScreenContainer(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    onSignOutClick: () -> Unit = {},
    onOpenImagePicker: () -> Unit = {},
) {
    SettingsScreen2(
        modifier = modifier,
        userName = userViewModel.me.name,
        imageRes = userViewModel.me.imageRes,
    )
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    PingTheme {
        SettingScreen()
    }
}

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    userName: String = "dude111",
    imageRes: Int = R.drawable.default_user,
    onSignOutClick: () -> Unit = {},
    onChangeProfilePicClick: () -> Unit = {},
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (signOutBtn, image, name, editImage) = createRefs()
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "image",
            modifier = Modifier
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 90.dp)
                }
                .clip(CircleShape)
                .size(100.dp)
        )
        TextButton(
            onClick = { onChangeProfilePicClick() },
            modifier = Modifier
                .constrainAs(editImage) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(image.bottom, margin = 90.dp)
                }
        ) {
            Text(text = "Change profile pic")
        }
        Text(
            text = userName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.outline,
            modifier = Modifier.constrainAs(name) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(image.bottom, margin = 30.dp)
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