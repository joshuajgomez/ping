package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewSignOutSetting() {
    PingTheme {
        SignOutSetting(rememberNavController())
    }
}

@Composable
fun SignOutSetting(
    navController: NavController,
    onSignOutClick: () -> Unit = {},
) {
    SettingContainer("Sign out", onCloseClick = { navController.popBackStack() }) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "Do you really want to sign out of ping?",
                fontSize = 20.sp,
                color = colorScheme.onSurface
            )
            var showLoading by remember { mutableStateOf(false) }
            PingButton(
                "Yes, sign out",
                icon = Icons.AutoMirrored.Default.ExitToApp,
                containerColor = colorScheme.onError,
                onClick = {
                    showLoading = true
                    onSignOutClick()
                }
            )
        }
    }
}