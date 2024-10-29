package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.joshgm3z.ping.graph.EditScreen
import com.joshgm3z.ping.ui.common.CustomTextField
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.common.PingButtonState
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.EditScreenUiState
import com.joshgm3z.ping.ui.viewmodels.EditScreenViewModel
import com.joshgm3z.ping.ui.viewmodels.EditType

fun NavController.navigateToEditScreen(editType: EditType) =
    navigate(EditScreen(editType))

@DarkPreview
@Composable
private fun PreviewEditScreen() {
    PingTheme {
        EditScreen()
    }
}

@Composable
fun EditScreen(
    viewModel: EditScreenViewModel? = getIfNotPreview { hiltViewModel() },
    goBack: () -> Unit = {},
) {
    val uiState = viewModel?.uiState?.collectAsState()?.value
        ?: EditScreenUiState()
    if (uiState.closeScreen) goBack()

    var text by remember { mutableStateOf(uiState.value) }
    SettingContainer(
        "Edit ${uiState.label}",
        isCloseEnabled = !uiState.isLoading,
        onCloseClick = goBack
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
        ) {
            CustomTextField(
                text = text,
                enabled = !uiState.isLoading,
                onTextChanged = { text = it }
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ErrorMessage(uiState.error)
                Spacer(Modifier.height(20.dp))
                Column {
                    val buttonModifier = Modifier.fillMaxWidth()
                    PingButton(
                        state = PingButtonState.Secondary,
                        text = "Cancel",
                        onClick = goBack,
                        modifier = buttonModifier
                    )
                    Spacer(Modifier.size(20.dp))
                    PingButton(
                        text = when {
                            uiState.isLoading -> "Updating"
                            else -> "Update"
                        },
                        onClick = { viewModel?.updateValue(text) },
                        state = when {
                            uiState.isLoading -> PingButtonState.Loading
                            else -> PingButtonState.Default
                        },
                        modifier = buttonModifier
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorMessage(error: String) {
    AnimatedVisibility(error.isNotEmpty()) {
        Row {
            Icon(
                Icons.Default.Warning,
                null,
                tint = colorScheme.error
            )
            Spacer(Modifier.width(10.dp))
            Text(
                error,
                color = colorScheme.error
            )
        }
    }
}
