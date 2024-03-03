package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.viewmodels.UserViewModel

@Composable
fun ImagePickerContainer(
    onGoBackClick: () -> Unit = {},
    userViewModel: UserViewModel,
) {
    val homeActivity = LocalContext.current as HomeActivity
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
    val imagePath by remember { mutableStateOf(defaultImagePath) }
    var selectedIcon by remember { mutableIntStateOf(defaultImageRes) }

    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (title, goBack, gallery, picker, done, remove) = createRefs()
        CustomIcon(
            iconRes = Icons.AutoMirrored.Default.ArrowBack,
            color = colorScheme.onSurface,
            onClick = {
                onGoBackClick()
            },
            modifier = Modifier.constrainAs(goBack) {
                start.linkTo(parent.start, margin = 10.dp)
                top.linkTo(parent.top, margin = 10.dp)
            }
        )
        if (imagePath.isNotEmpty() || selectedIcon != 0) {
            CustomIcon(
                iconRes = Icons.Default.Done,
                onClick = {
                    onImageConfirmed(selectedIcon)
                    onGoBackClick()
                },
                modifier = Modifier.constrainAs(done) {
                    end.linkTo(parent.end, margin = 10.dp)
                    top.linkTo(parent.top, margin = 10.dp)
                }
            )
        }
        Text(
            text = "Pick your profile pic",
            color = colorScheme.onSurface,
            fontSize = 23.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(goBack.end, margin = 10.dp)
                top.linkTo(goBack.top, margin = 7.dp)
            }
        )
        if (imagePath.isEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(130.dp),
                modifier = Modifier.constrainAs(gallery) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(title.bottom, margin = 30.dp)
                }
            ) {
                items(items = stockImageList) { it ->
                    ImageItem(
                        imageRes = it,
                        onIconClick = { imageRes -> selectedIcon = imageRes },
                        isSelected = selectedIcon == it
                    )
                }
            }
            Picker(
                onOpenCamera = { onOpenCamera() },
                onOpenMedia = { onOpenMedia() },
                modifier = Modifier.constrainAs(picker) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(gallery.bottom, margin = 80.dp)
                })
        } else {
            Image(
                painter = painterResource(id = R.drawable.default_user),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .constrainAs(gallery) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(title.bottom, margin = 60.dp)
                    }
            )
            CustomIcon(
                iconRes = Icons.Default.Clear,
                color = colorScheme.error,
                onClick = { onImageConfirmed(selectedIcon) },
                modifier = Modifier
                    .constrainAs(remove) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(gallery.bottom, margin = 60.dp)
                    }
                    .clip(CircleShape)
                    .background(color = colorScheme.errorContainer)
                    .size(60.dp)
            )
        }
    }
}

@Composable
fun CustomIcon(
    iconRes: ImageVector = Icons.Default.Done,
    color: Color = colorScheme.primary,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier
    ) {
        Icon(
            imageVector = iconRes,
            contentDescription = null,
            tint = color,
        )
    }
}

@Composable
fun Picker(
    modifier: Modifier = Modifier,
    onOpenCamera: () -> Unit,
    onOpenMedia: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        IconButton(
            onClick = { onOpenCamera() },
            modifier = Modifier
                .clip(CircleShape)
                .background(colorScheme.outlineVariant)
                .padding(all = 15.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                tint = colorScheme.onSurface,
                modifier = Modifier.size(50.dp)
            )
        }
        IconButton(
            onClick = { onOpenMedia() },
            modifier = Modifier
                .clip(CircleShape)
                .background(colorScheme.outlineVariant)
                .padding(all = 15.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = null,
                tint = colorScheme.onSurface,
                modifier = Modifier.size(50.dp)
            )
        }
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

