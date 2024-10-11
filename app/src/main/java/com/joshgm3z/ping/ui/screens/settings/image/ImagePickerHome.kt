package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshgm3z.ping.R
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

@Preview
@Composable
private fun PreviewImagePreview() {
    PingTheme {
        ImagePreviewer()
    }
}

@Composable
fun ImagePickerHome(
    userViewModel: UserViewModel,
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

@Composable
private fun ImagePicker(
    onOpenCameraClick: () -> Unit = {},
    onOpenGalleryClick: () -> Unit = {},
) {
    var selectedImage = remember { mutableStateOf("") }
    if (selectedImage.value.isEmpty()) {
        PickerButtons(onOpenCameraClick, onOpenGalleryClick)
    } else {
        ImagePreviewer(selectedImage.value)
    }
}

@Composable
fun PickerButtons(
    onOpenCameraClick: () -> Unit = {},
    onOpenGalleryClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) {
        BrowseButton(onOpenGalleryClick)
        Spacer(Modifier.height(30.dp))
        OpenCamera(onOpenCameraClick)
    }
}

@Composable
fun IconPicker(
    defaultImageRes: Int = 1,
) {
    var selectedIcon by remember { mutableIntStateOf(defaultImageRes) }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        IconGrid(
            onIconClick = {
                selectedIcon = it
            },
            selectedIcon
        )
        PingButton("Save icon")
    }
}

@Composable
fun ImagePreviewer(
    selectedImage: String = "",
    isShowLoading: Boolean = false,
    onClickRetake: () -> Unit = {},
    onClickDelete: () -> Unit = {},
    onClickSave: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.default_user),
                contentDescription = null,
                modifier = Modifier.clip(shape = CircleShape)
            )
            Spacer(Modifier.height(20.dp))
            PingButton(
                "Retake",
                onClick = onClickRetake,
                icon = Icons.Default.Replay,
                containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
            )
            PingButton(
                "Remove",
                onClick = onClickDelete,
                icon = Icons.Default.Delete,
                containerColor = colorScheme.onError
            )
        }
        PingButton(
            "Save",
            onClick = onClickSave,
            isShowLoading = isShowLoading
        )
    }
}
