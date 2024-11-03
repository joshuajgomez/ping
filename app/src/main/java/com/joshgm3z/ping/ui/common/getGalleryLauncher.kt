package com.joshgm3z.ping.ui.common

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.joshgm3z.utils.Logger

@Composable
fun getGalleryLauncher(
    onUriReady: (uri: Uri) -> Unit
): ManagedActivityResultLauncher<String, Uri?> = rememberLauncherForActivityResult(
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

fun ManagedActivityResultLauncher<String, Uri?>.launchImagePicker() {
    launch("image/*")
}

fun ManagedActivityResultLauncher<String, Uri?>.launchFilePicker() {
    launch("application/*")
}
