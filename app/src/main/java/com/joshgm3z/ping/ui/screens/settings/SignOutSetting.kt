package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.ping.graph.Frx
import com.joshgm3z.ping.graph.GoodBye
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel

@DarkPreview
@Composable
private fun PreviewSignOutSetting() {
    PingTheme {
        SignOutSetting()
    }
}

@Composable
fun SignOutSetting(
    userViewModel: UserViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onLoggedOut: () -> Unit = {},
) {
    var showLoading by remember { mutableStateOf(false) }
    SettingContainer(
        title = "Sign out",
        onCloseClick = onBackClick,
        isCloseEnabled = !showLoading
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "Do you really want to sign out of ping?",
                fontSize = 20.sp,
                color = colorScheme.onSurface
            )
            Column {
                PingButton(
                    "Yes, sign out",
                    icon = Icons.AutoMirrored.Default.ExitToApp,
                    containerColor = colorScheme.onError,
                    isShowLoading = showLoading,
                    onClick = {
                        showLoading = true
                        userViewModel.onSignOutClicked(onLoggedOut)
                    }
                )
                Spacer(Modifier.height(20.dp))
                PingButton(
                    "No, keep me signed in",
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    onClick = onBackClick
                )
            }
        }
    }
}