package com.joshgm3z.ping.ui.screens.chat

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.TwoPingButtons
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewImagePreview() {
    PingTheme {
        ImagePreview()
    }
}

@Composable
fun ImagePreview(
    imageUri: Uri = Uri.parse(""),
    onBackClick: () -> Unit = {},
    onSendClick: () -> Unit = {}
) {
    SettingContainer(
        "Add image",
        onCloseClick = onBackClick
    ) {
        ImagePreviewContent(
            imageUri = imageUri,
            onSendClick = onSendClick,
            onCancelClick = onBackClick,
        )
    }
}

@Composable
fun ImagePreviewContent(
    imageUri: Uri,
    onSendClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
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
        Spacer(Modifier.height(50.dp))
        TwoPingButtons(
            text1 = "Add image",
            onClick1 = onSendClick,
            onClick2 = onCancelClick
        )
    }
}
