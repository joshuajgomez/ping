package com.joshgm3z.ping.ui.screens.settings.image

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.screens.settings.Setting
import com.joshgm3z.ping.ui.screens.settings.SettingListCard
import com.joshgm3z.ping.ui.theme.Green50
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Red10
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.utils.FileUtil
import com.joshgm3z.utils.Logger

@DarkPreview
@Composable
fun PreviewImagePicker() {
    PingTheme {
        ImagePicker()
    }
}

@Composable
fun ImagePicker(
    closePicker: () -> Unit = {},
    userViewModel: UserViewModel? = getIfNotPreview { hiltViewModel() },
) {
    userViewModel?.updateCurrentUser()
    var imageUrl by remember { mutableStateOf(userViewModel?.me?.imagePath) }
    Logger.debug("imageUrl=$imageUrl")

    val galleryLauncher = getGalleryLauncher {
        imageUrl = it.toString()
    }
    val cameraUri = getIfNotPreview { FileUtil.getUri(LocalContext.current) }
    val cameraLauncher = getCameraLauncher {
        imageUrl = cameraUri.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            UserImage(
                Modifier.size(220.dp),
                imageUrl ?: ""
            )
            Spacer(Modifier.height(30.dp))
            val settingList = listOf(
                Setting(
                    "Open Gallery", "Select a picture from your gallery",
                    Icons.Default.PhotoLibrary
                ) {
                    galleryLauncher.launch("image/*")
                },
                Setting(
                    "Take a picture", "Open device camera to take a new photo",
                    Icons.Default.CameraAlt
                ) {
                    cameraLauncher.launch(cameraUri!!)
                },
                Setting(
                    "Remove picture", "Remove your picture from ping profile",
                    Icons.Default.DeleteForever,
                    color = Red10,
                ) {
                    imageUrl = ""
                },
                Setting(
                    "Save picture", "Save as your new ping photo",
                    Icons.Default.Save,
                    color = Green50,
                    enabled = imageUrl?.startsWith("content") == true
                ) {
                    userViewModel?.saveImage(
                        imageUrl!!,
                        onProgress = {},
                        onImageSaved = {
                            closePicker()
                        },
                        onFailure = {},
                    )
                },
            )
            SettingListCard(settingList)
        }

        /*AnimatedVisibility(imageUrl?.startsWith("content") == true) {
            var buttonState: ButtonState by remember { mutableStateOf(ButtonState.Idle) }
            SaveButton(buttonState) {
                userViewModel?.saveImage(
                    imageUrl!!,
                    onProgress = {
                        buttonState = when (it) {
                            100f -> ButtonState.Success
                            else -> ButtonState.Saving(it)
                        }
                    },
                    onImageSaved = {
                        closePicker()
                    },
                    onFailure = {
                        buttonState = ButtonState.Idle
                    },
                )
            }
        }*/
    }
}

@Composable
fun getGalleryLauncher(
    onUriReady: (uri: Uri) -> Unit
): ManagedActivityResultLauncher<String, Uri?> =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            /**
             * Sample uri =
             * content://media/picker_get_content/0/com.google.android.apps.photos.cloudpicker/
             * media/1e3615cd-105d-4915-916f-96799581c7b8-1_all_20213
             */
            Logger.debug("onResult=$uri")
            uri?.let {
                onUriReady(it)
            }
        }
    )

@Composable
fun getCameraLauncher(
    onUriReady: () -> Unit
): ManagedActivityResultLauncher<Uri, Boolean> =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            Logger.debug("onResult success=$success")
            if (success) {
                onUriReady()
            }
        }
    )

//@DarkPreview
@Composable
fun PreviewPickerButtons() {
    PingTheme {
        PickerButtons()
    }
}

@Composable
fun PickerButtons(
    onOpenCameraClick: () -> Unit = {},
    onOpenGalleryClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(50.dp)
    ) {
        BrowseButton(onOpenGalleryClick)
        Spacer(Modifier.height(30.dp))
        OpenCamera(onOpenCameraClick)
    }
}