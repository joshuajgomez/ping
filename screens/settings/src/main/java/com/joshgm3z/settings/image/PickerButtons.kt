package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.dashedBorder
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
fun ImageUploaderPreview() {
    PingTheme {
        Column(
            modifier = Modifier
                .background(colorScheme.surface)
                .fillMaxSize()
        ) {
            BrowseButton()
            OpenCamera()
        }
    }
}

@Composable
fun BrowseButton(
    onOpenGalleryClick: () -> Unit = {}
) {
    TextButton(
        onOpenGalleryClick,
        modifier = Modifier
            .fillMaxWidth()
            .dashedBorder(),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.PhotoLibrary,
                contentDescription = null,
                tint = colorScheme.onSurface
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "Select a photo",
                textAlign = TextAlign.Center,
                color = colorScheme.onSurface
            )
        }
    }
}

@Composable
fun OpenCamera(onOpenCameraClick: () -> Unit = {}) {
    TextButton(
        onOpenCameraClick,
        modifier = Modifier
            .background(
                color = colorScheme.primaryContainer,
                shape = RoundedCornerShape(30.dp)
            )
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        Icon(
            Icons.Default.CameraAlt,
            contentDescription = null
        )
        Spacer(Modifier.width(10.dp))
        Text(
            "Open camera",
        )
    }
}
