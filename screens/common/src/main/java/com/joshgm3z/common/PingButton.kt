package com.joshgm3z.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Icecream
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewPingButton() {
    PingTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf(
                PingButtonState.Default(),
                PingButtonState.Default(true),
                PingButtonState.Accent(),
                PingButtonState.Accent(true),
                PingButtonState.Secondary(),
                PingButtonState.Secondary(true),
                PingButtonState.Error(),
                PingButtonState.Error(true),
                PingButtonState.WithIcon(icon = Icons.Default.Icecream),
                PingButtonState.WithIcon(icon = Icons.Default.Icecream, isLoading = true),
            ).forEach {
                PingButton(state = it)
            }
        }
    }
}

sealed class PingButtonState(val showLoading: Boolean) {
    data class Default(val isLoading: Boolean = false) : PingButtonState(isLoading)
    data class Accent(val isLoading: Boolean = false) : PingButtonState(isLoading)
    data class Secondary(val isLoading: Boolean = false) : PingButtonState(isLoading)
    data class Error(val isLoading: Boolean = false) : PingButtonState(isLoading)
    data class WithIcon(
        val icon: ImageVector,
        val isLoading: Boolean = false
    ) : PingButtonState(isLoading)
}

@Composable
fun PingButton(
    text: String = "Confirm",
    state: PingButtonState = PingButtonState.Default(),
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
    onClick: () -> Unit = {},
) {
    val color: Color
    val containerColor: Color
    when (state) {
        is PingButtonState.Error -> {
            color = colorScheme.error
            containerColor = colorScheme.errorContainer
        }

        is PingButtonState.Secondary -> {
            color = colorScheme.onSurface
            containerColor = colorScheme.surfaceContainerHigh
        }

        is PingButtonState.Accent -> {
            color = colorScheme.primaryContainer
            containerColor = colorScheme.primary
        }

        else -> {
            color = colorScheme.surface
            containerColor = colorScheme.onSurface
        }
    }
    PingButtonContent(
        modifier = modifier.height(50.dp),
        containerColor = containerColor,
        onClick = onClick,
        color = color,
        state = state,
        content = {
            when (state) {
                is PingButtonState.WithIcon -> {
                    Icon(
                        imageVector = state.icon,
                        contentDescription = null
                    )
                    Spacer(Modifier.size(10.dp))
                    Text(text, color = color)
                }

                else -> Text(text, color = color)
            }
        }
    )
}

@DarkPreview
@Composable
private fun PreviewTwoPingButtons() {
    PingTheme {
        TwoPingButtons()
    }
}

@Composable
fun TwoPingButtons(
    text1: String = "Confirm",
    loading1: Boolean = false,
    loading2: Boolean = false,
    text2: String = "Cancel",
    onClick1: () -> Unit = {},
    onClick2: () -> Unit = {},
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        val modifier: Modifier = Modifier.fillMaxWidth()
        PingButton(
            text = text1,
            onClick = onClick1,
            modifier = modifier,
            state = PingButtonState.Default(loading1)
        )
        PingButton(
            text = text2,
            onClick = onClick2,
            modifier = modifier,
            state = PingButtonState.Secondary(loading2)
        )
    }
}

@Composable
private fun PingButtonContent(
    modifier: Modifier,
    onClick: () -> Unit,
    color: Color,
    containerColor: Color,
    state: PingButtonState,
    content: @Composable () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = containerColor,
        )
    ) {
        when {
            state.showLoading -> CircularProgressIndicator(
                color = color,
                modifier = Modifier.size(30.dp)
            )

            else -> content()
        }
    }
}