package com.joshgm3z.ping.ui.screens.settings.image

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.theme.PingTheme

@Preview
@Composable
private fun PreviewImagePreview() {
    PingTheme {
        ImagePreviewer()
    }
}

@Composable
fun ImagePreviewer(
    imageUri: Uri = Uri.parse(""),
    isShowLoading: Boolean = false,
    onClickRetake: () -> Unit = {},
    onClickDelete: () -> Unit = {},
    onClickSave: () -> Unit = {},
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
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight()
                    .width(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.height(20.dp))
            PingButton(
                "Retake",
                onClick = onClickRetake,
                icon = Icons.Default.Replay,
                containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
            )
            PingButton(
                "Remove",
                onClick = onClickDelete,
                icon = Icons.Default.Delete,
                containerColor = colorScheme.onError
            )
        }
        PingButton(
            "Save",
            onClick = onClickSave,
            isShowLoading = isShowLoading
        )
    }
}