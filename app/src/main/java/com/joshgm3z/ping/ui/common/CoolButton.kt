package com.joshgm3z.ping.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewCoolButton() {
    PingTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            CoolButton(progress = 20f)
            CoolButton(progress = 40f)
            CoolButton(progress = 60f)
            CoolButton(progress = 80f)
            CoolButton(progress = 100f)
            PingButton("Download")
        }
    }
}

@Composable
fun CoolButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
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
            .clip(RoundedCornerShape(30.dp))
            .height(50.dp)
            .fillMaxWidth()
            .clickable(enabled) { onClick() },
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
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = contentColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ButtonBg(progress: Float, trackerColor: Color, bgColor: Color) {
    AnimatedVisibility(true) {
        Row {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(trackerColor)
                    .weight(progress + 0.1f)
            ) {}
            Box(
                Modifier
                    .fillMaxSize()
                    .background(bgColor)
                    .weight(100.1f - progress)
            ) {}
        }
    }
}
