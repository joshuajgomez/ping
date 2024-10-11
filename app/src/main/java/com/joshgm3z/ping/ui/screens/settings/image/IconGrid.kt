package com.joshgm3z.ping.ui.screens.settings.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.joshgm3z.ping.R

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
        columns = GridCells.Adaptive(110.dp),
    ) {
        items(items = stockImageList) {
            ImageItem(
                imageRes = it, onIconClick = onIconClick, isSelected = selectedItem == it
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
                width = if (isSelected) 10.dp else 0.dp, color = Color.Red, shape = CircleShape
            )
            .clickable { onIconClick(imageRes) },
    )
}

