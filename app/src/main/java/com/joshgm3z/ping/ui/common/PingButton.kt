package com.joshgm3z.ping.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshgm3z.ping.ui.theme.PingTheme

@Preview
@Composable
private fun PreviewPingButton() {
    PingTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf(
                PingButtonState.Default,
                PingButtonState.Secondary,
                PingButtonState.Loading,
                PingButtonState.Error,
                PingButtonState.ErrorLoading,
                PingButtonState.WithIcon(),
            ).forEach {
                PingButton(state = it)
            }
        }
    }
}

sealed class PingButtonState {
    data object Default : PingButtonState()
    data object Secondary : PingButtonState()
    data object Loading : PingButtonState()
    data object Error : PingButtonState()
    data object ErrorLoading : PingButtonState()
    data class WithIcon(val icon: ImageVector = Icons.Default.Replay) : PingButtonState()
}

@Composable
fun PingButton(
    text: String = "Confirm",
    state: PingButtonState = PingButtonState.Default,
    modifier: Modifier = Modifier.fillMaxWidth().padding(10.dp),
    onClick: () -> Unit = {},
) {
    val color: Color
    val containerColor: Color
    when (state) {
        is PingButtonState.Error, is PingButtonState.ErrorLoading -> {
            color = colorScheme.error
            containerColor = colorScheme.errorContainer
        }

        is PingButtonState.Secondary -> {
            color = colorScheme.onPrimaryContainer
            containerColor = colorScheme.primaryContainer
        }

        else -> {
            color = colorScheme.onPrimary
            containerColor = colorScheme.primary
        }
    }
    PingButtonContent(
        modifier = modifier
            .height(40.dp)
        ,
        containerColor = containerColor,
        onClick = onClick,
        content = {
            when (state) {
                is PingButtonState.Loading,
                is PingButtonState.ErrorLoading -> CircularProgressIndicator(
                    color = color,
                    modifier = Modifier.size(25.dp)
                )

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

@Composable
private fun PingButtonContent(
    modifier: Modifier,
    onClick: () -> Unit,
    containerColor: Color,
    content: @Composable () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = containerColor,
        )
    ) {
        content()
    }
}