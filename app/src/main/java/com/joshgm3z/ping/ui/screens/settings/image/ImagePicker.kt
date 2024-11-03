package com.joshgm3z.ping.ui.screens.settings.image

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.common.getCameraLauncher
import com.joshgm3z.ping.ui.common.getGalleryLauncher
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.common.launchImagePicker
import com.joshgm3z.ping.ui.screens.settings.Setting
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.screens.settings.SettingListCard
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Red20
import com.joshgm3z.ping.ui.viewmodels.ImagePickerUiState
import com.joshgm3z.ping.ui.viewmodels.ImagePickerViewModel
import com.joshgm3z.utils.FileUtil
import com.joshgm3z.utils.Logger

@DarkPreview
@Composable
fun PreviewImagePicker() {
    PingTheme {
        ImagePickerContainer()
    }
}

@Composable
fun ImagePickerContainer(
    closePicker: () -> Unit = {},
) {
    Box(Modifier.fillMaxSize()) {
        var showIconPicker by remember { mutableStateOf(false) }
        var selectedImage by remember { mutableStateOf<String?>(null) }
        Logger.warn("selectedImage=$selectedImage")
        SettingContainer("Choose picture", onCloseClick = closePicker) {
            ImagePicker(
                selectedImage,
                openIconPicker = {
                    showIconPicker = true
                })
        }
        if (showIconPicker) {
            IconPicker(
                onIconPicked = {
                    selectedImage = it
                    showIconPicker = false
                },
                onCloseClick = {
                    showIconPicker = false
                }
            )
        }
    }
}

@Composable
private fun ImagePicker(
    selectedImage: String? = null,
    openIconPicker: () -> Unit = {},
    viewModel: ImagePickerViewModel? = getIfNotPreview { hiltViewModel() },
) {
    LaunchedEffect(selectedImage) {
        selectedImage?.let {
            viewModel?.setSelectedImage(it)
        }
    }
    val uiState = viewModel?.uiState?.collectAsState()
    ImagePickerContent(
        uiState?.value ?: ImagePickerUiState(""),
        saveImage = {
            viewModel?.saveImage(it)
        },
        openIconPicker = openIconPicker,
        removeImage = { viewModel?.removeImage() }
    )
}

@Composable
fun ImagePickerContent(
    uiState: ImagePickerUiState,
    saveImage: (Uri) -> Unit,
    removeImage: () -> Unit,
    openIconPicker: () -> Unit,
) {
    Logger.debug("uiState = $uiState")
    val galleryLauncher = getGalleryLauncher {
        saveImage(it)
    }
    val cameraUri = getIfNotPreview { FileUtil.getUri(LocalContext.current) }
    val cameraLauncher = getCameraLauncher {
        saveImage(cameraUri ?: Uri.parse(""))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                UserImage(
                    imageUrl = uiState.imageUrl
                )
                if (uiState.loading) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color = Color.Black.copy(alpha = 0.5f))
                            .fillMaxSize()
                    )
                    CircularProgressIndicator()
                }
            }
            Spacer(Modifier.height(30.dp))
            val settingList = mutableListOf(
                Setting(
                    "Open Gallery", "Select a picture from your gallery",
                    Icons.Default.PhotoLibrary
                ) {
                    galleryLauncher.launchImagePicker()
                },
                Setting(
                    "Take a picture", "Open device camera for a selfie",
                    Icons.Default.CameraAlt
                ) {
                    cameraLauncher.launch(cameraUri!!)
                },
                Setting(
                    "Choose an icon", "Choose an icon for your profile",
                    Icons.Default.EmojiEmotions
                ) {
                    openIconPicker()
                },
            )
            SettingListCard(settingList)

            val settingList2 = mutableListOf(
                Setting(
                    "Remove picture",
                    icon = Icons.Default.DeleteForever,
                    textColor = Red20,
                    action = removeImage
                ),
            )

            Spacer(Modifier.height(20.dp))
            SettingListCard(settingList2)

            Spacer(Modifier.height(20.dp))
            InfoText(uiState.notify)
        }
    }
}

@Composable
fun InfoText(message: String) {
    AnimatedVisibility(message.isNotEmpty()) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(colorScheme.primary, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(Modifier.width(15.dp))
            Text(message, color = colorScheme.onSurface)
        }
    }
}

