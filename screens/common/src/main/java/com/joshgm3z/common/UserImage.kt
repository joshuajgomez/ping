package com.joshgm3z.common

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.data.util.randomUser

@DarkPreview
@Composable
private fun PreviewUserImage() {
    PingTheme {
        UserImage()
    }
}

@DarkPreview
@Composable
private fun PreviewUserImageIcon() {
    PingTheme {
        UserImage()
    }
}

@Composable
fun UserImage(
    modifier: Modifier = Modifier.size(250.dp),
    imageUrl: String = randomUser().imagePath,
) {
    OnlineImage(
        model = imageUrl,
        placeholder = painterResource(R.drawable.default_user),
        error = painterResource(R.drawable.default_user),
        modifier = modifier.clip(CircleShape),
    )
}
