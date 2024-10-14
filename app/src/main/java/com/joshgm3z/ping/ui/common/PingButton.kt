package com.joshgm3z.ping.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.ping.ui.theme.PingTheme

@Preview
@Composable
private fun ButtonWithLoadingPreview() {
    PingTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PingButton(
                "Retake",
                icon = Icons.Default.Replay,
                containerColor = colorScheme.surfaceContainerHigh
            )
            PingButton("Doing")
            PingButton("Doing", isShowLoading = true)
            PingButton(
                "Remove",
                icon = Icons.Default.Delete,
                containerColor = colorScheme.onError
            )
        }
    }
}

@Composable
fun PingButton(
    text: String,
    onClick: () -> Unit = {},
    icon: ImageVector? = null,
    textColor: Color = colorScheme.onSurface,
    containerColor: Color = colorScheme.surfaceContainerHighest,
    isShowLoading: Boolean = false,
    progress: Float = 0f,
) {
    Button(
        onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = containerColor
        )
    ) {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (center, right) = createRefs()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(center) {
                    centerTo(parent)
                }) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = textColor,
                        modifier = Modifier
                            .size(35.dp)
                            .padding(end = 10.dp)
                    )
                }
                Text(
                    text,
                    color = textColor,
                )
            }
            if (isShowLoading) {
                if (progress != 0f) {
                    // determinate
                    CircularProgressIndicator(
                        progress = { progress },
                        color = textColor,
                        modifier = Modifier
                            .constrainAs(right) {
                                end.linkTo(parent.end)
                            }
                            .size(30.dp))
                } else {
                    // indeterminate
                    CircularProgressIndicator(
                        color = textColor,
                        modifier = Modifier
                            .constrainAs(right) {
                                end.linkTo(parent.end)
                            }
                            .size(30.dp))
                }
            }
        }
    }
}