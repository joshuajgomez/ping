package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.CoolButton
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.theme.Green50
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.utils.Logger

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
        else -> {}
    }
}

sealed class ButtonState {
    data object Hide : ButtonState()
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
        bgColor = Green50
    )
}

@Composable
fun getUserViewModel(): UserViewModel? = when {
    LocalInspectionMode.current -> null
    else -> hiltViewModel<UserViewModel>()
}