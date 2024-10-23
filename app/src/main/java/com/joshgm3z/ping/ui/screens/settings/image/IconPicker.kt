package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.IconPickerUiState
import com.joshgm3z.ping.ui.viewmodels.IconPickerViewModel

@DarkPreview
@Composable
private fun PreviewIconPicker() {
    PingTheme {
        IconPicker()
    }
}

@Composable
fun IconPicker(
    viewModel: IconPickerViewModel? = getIfNotPreview { hiltViewModel() },
    goBack: () -> Unit = {},
    onIconPicked: (String) -> Unit = {},
) {
    SettingContainer(
        "Choose an icon",
        scrollable = false,
        onCloseClick = goBack
    ) {
        with(
            viewModel?.uiState
                ?.collectAsState()
                ?.value
        ) {
            when (this) {
                is IconPickerUiState.Fetching -> Text("Fetching available images")
                is IconPickerUiState.Empty -> Text("Sorry, no images found in server")
                is IconPickerUiState.Ready -> IconPickerContent(
                    currentImageUrl,
                    imageUrls,
                    onIconPicked
                )

                else -> Text("Unable to fetch images from server")
            }
        }
    }
}

@Composable
fun IconPickerContent(
    currentImageUrl: String,
    imageUrls: List<String>,
    onSaveClick: (String) -> Unit,
) {
    var selectedImage by remember { mutableStateOf(currentImageUrl) }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        IconGrid(
            onImageClick = {
                selectedImage = it
            },
            selectedImage,
            imageUrls
        )
        PingButton("Save icon", onClick = {
            onSaveClick(selectedImage)
        })
    }
}

@Composable
fun IconGrid(
    onImageClick: (String) -> Unit,
    selectedItem: String,
    imageUrls: List<String>,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
    ) {
        items(items = imageUrls) {
            IconItem(
                imageUrl = it,
                onIconClick = { onImageClick(it) },
                isSelected = selectedItem == it
            )
        }
    }
}

@Composable
private fun IconItem(
    imageUrl: String = "",
    onIconClick: () -> Unit = {},
    isSelected: Boolean = true,
) {
    AsyncImage(
        model = imageUrl,
        error = painterResource(R.drawable.default_user2),
        contentDescription = null,
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp)
            .clip(CircleShape)
            .border(
                width = if (isSelected) 10.dp else 0.dp, color = Color.Red, shape = CircleShape
            )
            .clickable { onIconClick() },
    )
}