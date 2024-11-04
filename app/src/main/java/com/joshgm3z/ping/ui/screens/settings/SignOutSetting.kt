package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.TwoPingButtons
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
                color = colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            val uiState = viewModel?.uiState?.collectAsState()
            with(uiState?.value) {
                when (this) {
                    is SignOutUiState.SignedOut -> Text(
                        "Good bye! You are now signed out.",
                        modifier = Modifier
                            .padding(bottom = 30.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    else -> TwoPingButtons(
                        text1 = "Sign out",
                        loading1 = this is SignOutUiState.Loading,
                        onClick1 = {
                            viewModel?.onSignOutClicked(onLoggedOut)
                        },
                        onClick2 = onBackClick
                    )
                }
            }
        }
    }
}