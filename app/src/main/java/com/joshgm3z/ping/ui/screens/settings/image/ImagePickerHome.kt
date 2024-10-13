package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel

@Preview
@Composable
private fun PreviewImagePickerHome() {
    PingTheme {
        SettingContainer("Choose a picture") {
            TabScreen()
        }
    }
}

@Preview
@Composable
private fun PreviewImagePickerHome1() {
    PingTheme {
        SettingContainer("Choose a picture") {
            TabScreen(1)
        }
    }
}

@Composable
fun ImagePickerHome(
    onGoBackClick: () -> Unit
) {
    SettingContainer("Choose a picture", onCloseClick = onGoBackClick) {
        TabScreen()
    }
}

@Composable
fun TabScreen(defaultTab: Int = 0) {
    var tabIndex by remember { mutableIntStateOf(defaultTab) }

    val tabs = listOf(Icons.Default.EmojiEmotions, Icons.Default.Image)

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = colorScheme.surfaceContainer,
            contentColor = colorScheme.onSurface,
        ) {
            tabs.forEachIndexed { index, icon ->
                Tab(
                    icon = {
                        Icon(icon, contentDescription = null)
                    },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    unselectedContentColor = colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        }
        when (tabIndex) {
            0 -> IconPicker()
            1 -> ImagePicker()
        }
    }
}


