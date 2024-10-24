package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.common.getIfNotPreview
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
    onIconPicked: (String) -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(520.dp)
                .background(
                    colorScheme.surface,
                    shape = RoundedCornerShape(20.dp)
                )
            ,
            contentAlignment = Alignment.Center
        ) {
            with(
                viewModel?.uiState
                    ?.collectAsState()
                    ?.value
            ) {
                when (this) {
                    is IconPickerUiState.Fetching -> CircularProgressIndicator()
                    is IconPickerUiState.Ready -> IconPickerContent(
                        currentImageUrl,
                        imageUrls,
                        onIconPicked,
                        onCloseClick
                    )

                    else -> IconPickerError()
                }
            }
        }
    }
}

@Composable
fun IconPickerError() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            Icons.Default.Error,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = colorScheme.onSurface
        )
        Spacer(Modifier.height(20.dp))
        Text("No icons found", color = colorScheme.onSurface, fontSize = 20.sp)
    }
}

@Composable
fun IconPickerContent(
    currentImageUrl: String = "",
    imageUrls: List<String> = listOf(),
    onSaveClick: (String) -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    var selectedImage by remember { mutableStateOf(currentImageUrl) }
    Column(
        modifier = Modifier.padding(20.dp)
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
        Spacer(Modifier.height(10.dp))
        IconGrid(
            onImageClick = {
                selectedImage = it
            },
            selectedImage,
            imageUrls
        )
        Spacer(Modifier.height(20.dp))
        PingButton("Choose", onClick = {
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
        columns = GridCells.Adaptive(110.dp),
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
            .padding(vertical = 5.dp, horizontal = 2.dp)
            .clip(CircleShape)
            .border(
                width = if (isSelected) 10.dp else 0.dp, color = Color.Red, shape = CircleShape
            )
            .clickable { onIconClick() },
    )
}