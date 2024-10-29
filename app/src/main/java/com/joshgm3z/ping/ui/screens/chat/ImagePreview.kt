package com.joshgm3z.ping.ui.screens.chat

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.common.PingButtonState
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewImagePreview() {
    PingTheme {
        ImagePreview(toName = "Alien")
    }
}

@Composable
fun ImagePreview(
    message: String = "",
    imageUri: Uri = Uri.parse(""),
    toName: String = "",
    onBackClick: () -> Unit = {},
    onSendClick: (String) -> Unit = {}
) {
    SettingContainer(
        "Send image",
        onCloseClick = onBackClick
    ) {
        ImagePreviewContent(
            imageUri = imageUri,
            buttonText = "Send to $toName",
            onSendClick = { onSendClick(message) }
        )
    }
}

@Composable
fun ImagePreviewContent(
    imageUri: Uri,
    buttonText: String,
    onSendClick: () -> Unit = {},
) {
    Column {
        AsyncImage(
            model = imageUri,
            error = painterResource(R.drawable.wallpaper2),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(5.dp))
        SendButton(
            buttonText,
            onClick = onSendClick
        )
    }
}

@Composable
private fun SendButton(
    text: String,
    onClick: () -> Unit = {},
) {
    PingButton(
        text,
        state = PingButtonState.WithIcon(
            Icons.AutoMirrored.Default.ArrowForward
        ),
        onClick = {
            onClick()
        },
    )
}