package com.joshgm3z.settings

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
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.SettingContainer
import com.joshgm3z.common.TwoPingButtons
import com.joshgm3z.common.getIfNotPreview
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.settings.viewmodels.SignOutUiState
import com.joshgm3z.settings.viewmodels.SignOutViewModel

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
    val uiState = viewModel?.uiState?.collectAsState()
    SettingContainer(
        title = "Sign out",
        onCloseClick = onBackClick,
        isCloseEnabled = uiState?.value == SignOutUiState.Initial,
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
            when {
                uiState?.value is SignOutUiState.SignedOut -> Text(
                    "Good bye! You are now signed out.",
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                else -> TwoPingButtons(
                    text1 = "Sign out",
                    loading1 = uiState?.value is SignOutUiState.Loading,
                    onClick1 = {
                        viewModel?.onSignOutClicked(onLoggedOut)
                    },
                    onClick2 = onBackClick
                )
            }
        }
    }
}