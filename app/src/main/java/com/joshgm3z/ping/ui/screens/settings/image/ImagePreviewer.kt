package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.theme.PingTheme

@Preview
@Composable
fun PreviewImagePreviewer() {
    PingTheme {
        ImagePreviewer()
    }
}

@Composable
fun ImagePreviewer(isShowLoading: Boolean = false) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(R.drawable.default_user),
            contentDescription = null,
            modifier = Modifier.clip(shape = CircleShape)
        )
        Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
            PingButton(
                "Retake",
                icon = Icons.Default.Replay,
                containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
            )
            PingButton(
                "Remove",
                icon = Icons.Default.Delete,
                containerColor = colorScheme.onError
            )
            Spacer(Modifier.height(10.dp))
            PingButton("Save", isShowLoading = isShowLoading)
        }
    }
}

