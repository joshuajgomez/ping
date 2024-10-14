package com.joshgm3z.ping.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewCoolButton() {
    PingTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CoolButton(progress = 20f)
            CoolButton(progress = 40f)
            CoolButton(progress = 60f)
            CoolButton(progress = 80f)
            CoolButton(progress = 100f)
        }
    }
}

@Composable
fun CoolButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    progress: Float = 0.0f,
    text: String = "Download",
    trackerColor: Color = colorScheme.surfaceContainerHighest,
    bgColor: Color = colorScheme.surfaceContainerHigh,
    contentColor: Color = colorScheme.onSurface,
    icon: ImageVector? = null,
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        ButtonBg(progress, trackerColor, bgColor)
        Row {
            icon?.let {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = contentColor
                )
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text,
                fontSize = 20.sp,
                color = contentColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ButtonBg(progress: Float, trackerColor: Color, bgColor: Color) {
    AnimatedVisibility(true) {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(30.dp))
        ) {
            Row(
                Modifier
                    .background(trackerColor)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(progress + 0.1f)
            ) {}
            Row(
                Modifier
                    .background(bgColor)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(100.1f - progress)
            ) {}
        }
    }
}
