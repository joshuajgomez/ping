package com.joshgm3z.ping.chat

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.DarkPreview
import com.joshgm3z.common.OnlineImage
import com.joshgm3z.common.SettingContainer
import com.joshgm3z.common.TwoPingButtons
import com.joshgm3z.common.defaultChatImage
import com.joshgm3z.common.theme.PingTheme

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
        OnlineImage(
            model = imageUri,
            error = defaultChatImage(),
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp)),
        )
        Spacer(Modifier.height(50.dp))
        TwoPingButtons(
            text1 = "Add image",
            onClick1 = onSendClick,
            onClick2 = onCancelClick
        )
    }
}
