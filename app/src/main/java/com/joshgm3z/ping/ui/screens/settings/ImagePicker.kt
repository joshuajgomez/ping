package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.joshgm3z.ping.HomeActivity
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.screens.settings.image.BrowseButton
import com.joshgm3z.ping.ui.screens.settings.image.ImagePreviewer
import com.joshgm3z.ping.ui.screens.settings.image.OpenCamera
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel

@Composable
fun ImagePickerContainer(
    onGoBackClick: () -> Unit = {},
    userViewModel: UserViewModel,
) {
    ImagePicker(
        onGoBackClick = { onGoBackClick() },
        onImageConfirmed = { userViewModel.onImageConfirmed(it) },
        defaultImageRes = userViewModel.me.imageRes,
        onOpenCamera = { },
        onOpenMedia = { },
    )
}

@Preview
@Composable
private fun PreviewImagePicker() {
    PingTheme {
        ImagePicker()
    }
}

@Preview
@Composable
private fun PreviewImagePickerSelected() {
    PingTheme {
        ImagePicker(defaultImageRes = stockImageList[5])
    }
}

@Preview
@Composable
private fun PreviewImagePickerPicked() {
    PingTheme {
        ImagePicker(defaultImagePath = "image")
    }
}

val stockImageList = listOf(
    R.drawable.stock_user_pic1,
    R.drawable.stock_user_pic2,
    R.drawable.stock_user_pic3,
    R.drawable.stock_user_pic4,
    R.drawable.stock_user_pic5,
    R.drawable.stock_user_pic6,
    R.drawable.stock_user_pic7,
    R.drawable.stock_user_pic8,
)

@Composable
private fun ImagePicker(
    onGoBackClick: () -> Unit = {},
    defaultImagePath: String = "",
    defaultImageRes: Int = 0,
    onImageConfirmed: (imageRes: Int) -> Unit = {},
    onOpenCamera: () -> Unit = {},
    onOpenMedia: () -> Unit = {},
) {

    SettingContainer("Choose image") {

        val imagePath by remember { mutableStateOf(defaultImagePath) }
        var selectedIcon by remember { mutableIntStateOf(defaultImageRes) }

        if (imagePath.isNotEmpty() || selectedIcon != 0) {
            ImagePreviewer()
        } else {
            Column {
                IconGrid(
                    onIconClick = {
                        selectedIcon = it
                    },
                    selectedIcon
                )
                Spacer(Modifier.height(50.dp))
                Picker(
                    onOpenCamera = { onOpenCamera() },
                    onOpenMedia = { onOpenMedia() },
                )
            }
        }
    }
}

@Composable
fun IconGrid(
    onIconClick: (imageRes: Int) -> Unit,
    selectedItem: Int
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(110.dp),
    ) {
        items(items = stockImageList) {
            ImageItem(
                imageRes = it,
                onIconClick = onIconClick,
                isSelected = selectedItem == it
            )
        }
    }
}

@Composable
fun Picker(
    modifier: Modifier = Modifier,
    onOpenCamera: () -> Unit,
    onOpenMedia: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        BrowseButton()
        Spacer(Modifier.height(10.dp))
        Text("or", fontSize = 20.sp)
        Spacer(Modifier.height(10.dp))
        OpenCamera()
    }
}

@Composable
private fun ImageItem(
    imageRes: Int = stockImageList.random(),
    onIconClick: (imageRes: Int) -> Unit = {},
    isSelected: Boolean = true,
) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp)
            .clip(CircleShape)
            .border(
                width = if (isSelected) 10.dp else 0.dp,
                color = Color.Red, shape = CircleShape
            )
            .clickable { onIconClick(imageRes) },
    )
}

