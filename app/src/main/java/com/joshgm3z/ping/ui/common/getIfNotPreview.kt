package com.joshgm3z.ping.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
fun <T> getIfNotPreview(value: @Composable () -> T): T? = when {
    LocalInspectionMode.current -> null
    else -> value.invoke()
}