package com.joshgm3z.ping.ui.screens.settings.image

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.utils.FileUtil
import com.joshgm3z.utils.Logger

@Composable
fun ImagePicker(
    closePicker: () -> Unit,
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            /**
             * Sample uri =
             * content://media/picker_get_content/0/com.google.android.apps.photos.cloudpicker/
             * media/1e3615cd-105d-4915-916f-96799581c7b8-1_all_20213
             */
            Logger.debug("onResult=$uri")
            uri?.let {
                imageUri = it
            }
        }
    )

    val file = FileUtil.createImageFile(LocalContext.current)
    val cameraUri = FileUtil.getUri(LocalContext.current, file)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            Logger.debug("onResult success=$success")
            if (success) {
                imageUri = cameraUri
            }
        }
    )

    if (imageUri == null) {
        PickerButtons(
            onOpenCameraClick = {
                cameraLauncher.launch(cameraUri)
            },
            onOpenGalleryClick = {
                galleryLauncher.launch("image/*")
            })
    } else {
        ImagePreviewer(
            imageUri = imageUri!!,
            onClickRetake = {
                imageUri = null
            },
            closePicker = closePicker
        )

    }
}

@DarkPreview
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
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) {
        BrowseButton(onOpenGalleryClick)
        Spacer(Modifier.height(30.dp))
        OpenCamera(onOpenCameraClick)
    }
}