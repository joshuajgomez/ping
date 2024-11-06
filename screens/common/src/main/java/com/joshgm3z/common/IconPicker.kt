package com.joshgm3z.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.common.viewmodels.IconPickerUiState
import com.joshgm3z.common.viewmodels.IconPickerViewModel

@DarkPreview
@Composable
private fun PreviewIconPicker2() {
    PingTheme {
        IconPickerContent(
            IconPickerUiState.Ready(
                "ss", listOf(
                    "", "", "",
                    "", "ss", "",
                    "", "",
                )
            )
        )
    }
}

@DarkPreview
@Composable
private fun PreviewIconPicker() {
    PingTheme {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            IconPickerContent(IconPickerUiState.Fetching)
            IconPickerContent(IconPickerUiState.Empty)
            IconPickerContent(IconPickerUiState.Error("Unable to reach server"))
        }
    }
}

@Composable
fun IconPicker(
    viewModel: IconPickerViewModel? = getIfNotPreview { hiltViewModel() },
    onIconPicked: (String) -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    Box(
        Modifier
            .padding(top = 100.dp)
            .background(Color.Black.copy(0.5f))
            .fillMaxSize()
            .clickable { onCloseClick() },
        contentAlignment = Alignment.Center
    ) {
        with(viewModel?.uiState?.collectAsState()?.value) {
            when {
                this != null -> IconPickerContent(
                    this, onIconPicked, onCloseClick
                )
            }
        }
    }
}

@Composable
fun IconPickerContent(
    uiState: IconPickerUiState,
    onSaveClick: (String) -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .background(
                colorScheme.surface,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Choose icon",
                color = colorScheme.onSurface,
                fontSize = 20.sp
            )
            IconButton(onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = colorScheme.onSurface,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        with(uiState) {
            when (this) {
                is IconPickerUiState.Fetching -> LoadingInfo()
                is IconPickerUiState.Error -> IconPickerError("Unable to reach server")
                is IconPickerUiState.Empty -> IconPickerError("No icons found")
                is IconPickerUiState.Ready -> IconGridContainer(
                    currentImageUrl,
                    imageUrls,
                    onSaveClick
                )
            }
        }
    }
}

@Composable
fun IconGridContainer(
    currentImageUrl: String,
    imageUrls: List<String>,
    onSaveClick: (String) -> Unit
) {
    var selectedImage by remember { mutableStateOf(currentImageUrl) }
    Column(
    ) {
        Spacer(Modifier.height(10.dp))
        IconGrid(
            onImageClick = {
                selectedImage = it
            },
            selectedImage,
            imageUrls
        )
        Spacer(Modifier.height(10.dp))
        PingButton(
            "Confirm",
            onClick = { onSaveClick(selectedImage) }
        )
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
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(110.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            error = painterResource(R.drawable.default_user),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 5.dp)
                .clip(CircleShape)
                .clickable { onIconClick() },
        )
        if (isSelected) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier
                        .background(
                            color = colorScheme.surface,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun IconPickerError(error: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
            .height(80.dp)
    ) {
        Icon(
            Icons.Default.Error,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = colorScheme.error
        )
        Spacer(Modifier.height(20.dp))
        Text(
            error,
            color = colorScheme.error
        )
    }
}

@Composable
fun LoadingInfo() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
            .height(80.dp)
    ) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(20.dp))
        Text(
            "Fetching icons",
            color = colorScheme.onSurface
        )
    }
}