package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel

@DarkPreview
@Composable
private fun PreviewImagePickerHome() {
    PingTheme {
        SettingContainer("Choose a picture") {
            TabScreen()
        }
    }
}

//@DarkPreview
@Composable
private fun PreviewImagePickerHome1() {
    PingTheme {
        SettingContainer("Choose a picture") {
            TabScreen(1)
        }
    }
}

@Composable
fun ImagePickerContainer(
    onGoBackClick: () -> Unit = {},
    userViewModel: UserViewModel = hiltViewModel(),
) {
    ImagePickerHome(
        onGoBackClick,
        onSaveClick = { userViewModel.onImageConfirmed(it) },
        goBack = onGoBackClick,
    )
}

@Composable
private fun ImagePickerHome(
    onGoBackClick: () -> Unit,
    onSaveClick: (icon: Int) -> Unit = {},
    goBack: () -> Unit = {},
) {
    SettingContainer("Choose a picture", onCloseClick = onGoBackClick) {
        TabScreen(
            onSaveClick = onSaveClick,
            goBack = goBack
        )
    }
}

@Composable
private fun TabScreen(
    defaultTab: Int = 0,
    onSaveClick: (icon: Int) -> Unit = {},
    goBack: () -> Unit = {},
) {
    ImagePicker(closePicker = goBack)
    /*var tabIndex by remember { mutableIntStateOf(defaultTab) }

    val tabs = listOf(Icons.Default.Image, Icons.Default.EmojiEmotions)

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onSurface,
        ) {
            tabs.forEachIndexed { index, icon ->
                Tab(
                    icon = { Icon(icon, contentDescription = null) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    unselectedContentColor = colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        }
        when (tabIndex) {
            0 -> ImagePicker(closePicker = goBack)
            1 -> IconPicker(onSaveClick = onSaveClick)
        }
    }*/
}


