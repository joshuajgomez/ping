package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.common.PingButtonState
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.SignOutUiState
import com.joshgm3z.ping.ui.viewmodels.SignOutViewModel

@DarkPreview
@Composable
private fun PreviewSignOutSetting() {
    PingTheme {
        SignOutSetting()
    }
}

@Composable
fun SignOutSetting(
    viewModel: SignOutViewModel? = getIfNotPreview { hiltViewModel() },
    onBackClick: () -> Unit = {},
    onLoggedOut: () -> Unit = {},
) {
    SettingContainer(
        title = "Sign out",
        onCloseClick = onBackClick,
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "Do you really want to sign out of ping?",
                color = colorScheme.onSurface
            )
            Row {
                val buttonModifier = Modifier
                    .weight(1f)
                    .padding(vertical = 20.dp)
                PingButton(
                    modifier = buttonModifier,
                    text = "Go back",
                    state = PingButtonState.Secondary,
                    onClick = onBackClick
                )
                Spacer(Modifier.size(20.dp))
                val buttonState = when (viewModel?.uiState?.collectAsState()?.value) {
                    is SignOutUiState.Loading -> PingButtonState.ErrorLoading
                    else -> PingButtonState.Error
                }
                PingButton(
                    modifier = buttonModifier,
                    text = "Sign out",
                    state = buttonState,
                    onClick = {
                        viewModel?.onSignOutClicked(onLoggedOut)
                    }
                )
            }
        }
    }
}