package com.joshgm3z.ping.ui.common

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.joshgm3z.utils.Logger

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