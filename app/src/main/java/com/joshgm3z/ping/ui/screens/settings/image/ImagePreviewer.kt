package com.joshgm3z.ping.ui.screens.settings.image

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel

@Preview
@Composable
private fun PreviewImagePreview() {
    PingTheme {
        ImagePreviewer()
    }
}


@Composable
fun ImagePreviewer(
    imageUrl: String = "",
    onClickRetake: () -> Unit = {},
    closePicker: () -> Unit = {},
    userViewModel: UserViewModel? = getUserViewModel()
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
            UserImage(
                Modifier.size(250.dp),
                imageUrl
            )
            Spacer(Modifier.height(20.dp))
            PingButton(
                "Retake",
                onClick = onClickRetake,
                icon = Icons.Default.Replay,
                containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
            )
        }
        var isShowLoading by remember { mutableStateOf(false) }
        var progress by remember { mutableFloatStateOf(0f) }
        PingButton(
            "Save",
            onClick = {
                isShowLoading = true
                userViewModel?.saveImage(
                    imageUrl,
                    onProgress = {
                        progress = it
                    },
                    onImageSaved = {
                        userViewModel.updateCurrentUser()
                        closePicker()
                    },
                    onFailure = {},
                )
            },
            isShowLoading = isShowLoading,
            progress = progress / 100
        )
    }
}

@Composable
fun getUserViewModel(): UserViewModel? = when {
    LocalInspectionMode.current -> null
    else -> hiltViewModel<UserViewModel>()
}