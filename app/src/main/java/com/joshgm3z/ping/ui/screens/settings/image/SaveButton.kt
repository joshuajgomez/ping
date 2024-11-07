package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.CoolButton
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.theme.Green50
import com.joshgm3z.common.theme.PingTheme
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
        else -> SuccessButton()
    }
}

sealed class ButtonState {
    data object Disabled : ButtonState()
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
        enabled = false,
        progress = progress
    )
}

@Composable
fun SuccessButton() {
    CoolButton(
        text = "Saved",
        enabled = false,
        icon = Icons.Default.CheckCircle,
        bgColor = Green50
    )
}
