package com.joshgm3z.ping.ui.screens.settings.image

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.EmojiEmotions
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
import com.joshgm3z.data.util.randomUser
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.UserImage
import com.joshgm3z.ping.ui.common.getCameraLauncher
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.screens.settings.Setting
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.screens.settings.SettingListCard
import com.joshgm3z.ping.ui.theme.Green50
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Red20
import com.joshgm3z.ping.ui.viewmodels.IconPickerViewModel
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
    openIconPicker: () -> Unit = {},
    viewModel: IconPickerViewModel? = getIfNotPreview { hiltViewModel() },
) {
    SettingContainer("Choose picture", onCloseClick = closePicker) {
        ImagePicker(closePicker, openIconPicker, viewModel)
    }
}

@Composable
private fun ImagePicker(
    closePicker: () -> Unit = {},
    openIconPicker: () -> Unit = {},
    viewModel: IconPickerViewModel? = getIfNotPreview { hiltViewModel() },
) {
    val defaultUser = viewModel?.me ?: randomUser()
    val user by remember { mutableStateOf(defaultUser) }
    Logger.debug("user=$user")

    var buttonState: ButtonState by remember { mutableStateOf(ButtonState.Disabled) }

    val galleryLauncher = getGalleryLauncher {
        user.imagePath = it.toString()
        buttonState = ButtonState.Idle
    }
    val cameraUri = getIfNotPreview { FileUtil.getUri(LocalContext.current) }
    val cameraLauncher = getCameraLauncher {
        user.imagePath = cameraUri.toString()
        buttonState = ButtonState.Idle
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            UserImage(
                Modifier.size(200.dp),
                user
            )
            Spacer(Modifier.height(30.dp))
            val settingList = mutableListOf(
                Setting(
                    "Open Gallery", "Select a picture from your gallery",
                    Icons.Default.PhotoLibrary
                ) {
                    galleryLauncher.launch("image/*")
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
                ) {
                    user.imagePath = ""
                },
                saveButtonSetting(buttonState) {
                    viewModel?.saveImage(
                        user.imagePath,
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
            )

            Spacer(Modifier.height(20.dp))
            SettingListCard(settingList2)

        }
    }
}

fun saveButtonSetting(
    buttonState: ButtonState,
    onSaveClick: () -> Unit
): Setting =
    when (buttonState) {
        is ButtonState.Disabled -> Setting(
            "Save picture",
            icon = Icons.Default.Save,
            enabled = false
        )

        is ButtonState.Idle -> Setting(
            "Save picture",
            "Save your picture to your profile",
            icon = Icons.Default.Save,
            action = onSaveClick
        )

        is ButtonState.Saving -> Setting(
            "Saving...",
            "Save your picture to your profile",
            icon = Icons.Default.CloudUpload,
        )

        is ButtonState.Success -> Setting(
            "Saved",
            "Save your picture to your profile",
            icon = Icons.Default.CheckCircle,
            textColor = Green50,
        )
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