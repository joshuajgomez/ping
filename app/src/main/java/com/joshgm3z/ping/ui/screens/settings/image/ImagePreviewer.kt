package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.CoolButton
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.theme.Green40
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.utils.Logger
import kotlinx.coroutines.delay

//@DarkPreview
@Composable
private fun PreviewImagePreview() {
    PingTheme {
        ImagePreviewer()
    }
}


@Composable
fun ImagePreviewer(
    imageUrl: String = "",
    onClickRetake: () -> Unit = {},
    closePicker: () -> Unit = {},
    userViewModel: UserViewModel? = getUserViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            UserImage(
                Modifier.size(250.dp),
                imageUrl
            )
            Spacer(Modifier.height(20.dp))
            PingButton(
                "Retake",
                onClick = onClickRetake,
                icon = Icons.Default.Replay,
                containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
            )
        }
        var buttonState: ButtonState by remember { mutableStateOf(ButtonState.Idle) }
        SaveButton(buttonState) {
            userViewModel?.saveImage(
                imageUrl,
                onProgress = {
                    buttonState = when (it) {
                        100f -> ButtonState.Success
                        else -> ButtonState.Saving(it)
                    }
                },
                onImageSaved = {
                    closePicker()
                },
                onFailure = {
                    buttonState = ButtonState.Idle
                },
            )
        }
    }
}

@Composable
fun SaveButton(
    buttonState: ButtonState,
    onClick: () -> Unit = {}
) {
    Logger.debug("save button recompose")
    when (buttonState) {
        is ButtonState.Idle -> CoolButton(
            text = "Save",
            onClick = onClick
        )

        is ButtonState.Saving -> SavingButton(buttonState.progress)
        is ButtonState.Success -> SuccessButton()
    }
}

sealed class ButtonState {
    data object Idle : ButtonState()
    data class Saving(val progress: Float) : ButtonState()
    data object Success : ButtonState()
}

@DarkPreview
@Composable
fun PreviewButtons() {
    PingTheme {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            SavingButton(40f)
            SuccessButton()
        }
    }
}

@Composable
fun SavingButton(progress: Float = 0f) {
    CoolButton(
        text = "Saving...",
        progress = progress
    )
}

@Composable
fun SuccessButton() {
    CoolButton(
        text = "Saved",
        icon = Icons.Default.CheckCircle,
        bgColor = Green40
    )
}

@Composable
fun getUserViewModel(): UserViewModel? = when {
    LocalInspectionMode.current -> null
    else -> hiltViewModel<UserViewModel>()
}