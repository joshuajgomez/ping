package com.joshgm3z.ping.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.ImagePreviewUiState
import com.joshgm3z.ping.ui.viewmodels.ImagePreviewViewModel

@DarkPreview
@Composable
private fun PreviewImagePreview() {
    PingTheme {
        ImagePreview()
    }
}

@Composable
fun ImagePreview(
    navController: NavController = rememberNavController(),
    viewModel: ImagePreviewViewModel? = getIfNotPreview { hiltViewModel() },
    onBackClick: () -> Unit = {
        navController.popBackStack()
    },
    onSendClick: () -> Unit = {
        viewModel?.onSendButtonClick()
    }
) {
    SettingContainer(
        "Send image",
        onCloseClick = onBackClick
    ) {
        val uiState = viewModel?.uiState?.collectAsState()
        with(uiState?.value) {
            when (this) {
                is ImagePreviewUiState.Initial -> {}
                is ImagePreviewUiState.Ready -> ImagePreviewContent(
                    imageUrl = imageUrl,
                    buttonText = "Send to $toName",
                    onSendClick = onSendClick
                )

                is ImagePreviewUiState.Sending -> ImagePreviewContent(
                    imageUrl = imageUrl,
                    buttonText = "Sending",
                    onSendClick = onSendClick
                )

                is ImagePreviewUiState.Sent -> onBackClick()

                else -> {}
            }
        }
    }
}

@Composable
fun ImagePreviewContent(
    imageUrl: String,
    buttonText: String,
    onSendClick: () -> Unit = {},
) {
    Column {
        AsyncImage(
            model = imageUrl,
            error = painterResource(R.drawable.wallpaper2),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(30.dp))
        SendButton(
            buttonText,
            onClick = onSendClick
        )
    }
}

@Composable
fun Recipient(name: String?) {
    if (name.isNullOrEmpty()) return
    Text(
        name,
        fontSize = 20.sp,
        color = colorScheme.onSurface,
        modifier = Modifier
            .background(
                color = colorScheme.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp, vertical = 3.dp)
    )
}

@Composable
private fun SendButton(
    text: String,
    onClick: () -> Unit = {},
) {
    PingButton(
        text,
        icon = Icons.AutoMirrored.Default.Send,
        onClick = {
            onClick()
        },
    )
}