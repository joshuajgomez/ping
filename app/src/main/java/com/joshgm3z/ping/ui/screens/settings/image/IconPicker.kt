package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joshgm3z.ping.R
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.common.ErrorText
import com.joshgm3z.ping.ui.common.PingButton
import com.joshgm3z.ping.ui.common.getIfNotPreview
import com.joshgm3z.ping.ui.screens.settings.SettingContainer
import com.joshgm3z.ping.ui.theme.PingTheme
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
    defaultImageRes: Int = viewModel?.me?.profileIcon ?: -1,
    goBack: () -> Unit = {},
) {
    SettingContainer(
        "Choose an icon",
        scrollable = false,
        onCloseClick = goBack
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
            Column {
                var errorText by remember { mutableStateOf("") }
                ErrorText(errorText)
                Spacer(Modifier.height(20.dp))
                var text by remember { mutableStateOf("Save icon") }
                PingButton("Save icon", onClick = {
                    text = "Saving"
                    errorText = ""
                    viewModel?.onImageConfirmed(
                        selectedIcon,
                        onImageSaved = goBack,
                        onFailure = {
                            errorText = it
                        }
                    )
                })
            }
        }
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
fun IconGrid(
    onIconClick: (imageRes: Int) -> Unit, selectedItem: Int
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
    ) {
        items(items = stockImageList) {
            IconItem(
                imageRes = it, onIconClick = onIconClick, isSelected = selectedItem == it
            )
        }
    }
}

@Composable
private fun IconItem(
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
                width = if (isSelected) 10.dp else 0.dp, color = Color.Red, shape = CircleShape
            )
            .clickable { onIconClick(imageRes) },
    )
}