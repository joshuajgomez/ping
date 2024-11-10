package com.joshgm3z.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
fun PreviewPingBottomSheet() {
    PingTheme {
        PingBottomSheet("Choose something")
    }
}

data class SheetOption(
    val title: String = "Camera",
    val icon: ImageVector = Icons.Default.CameraAlt,
    val onClick: () -> Unit = {},
)

val list = listOf(
    SheetOption(),
    SheetOption(),
    SheetOption(),
    SheetOption(),
)

@Composable
fun PingBottomSheet(
    title: String,
    sheetOptions: List<SheetOption> = list,
    onDismiss: () -> Unit = {},
) {
    Box(
        Modifier
            .clickable { onDismiss() }
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.BottomCenter
    ) {
        val topRadius = 30.dp
        ElevatedCard(
            shape = RoundedCornerShape(
                topStart = topRadius,
                topEnd = topRadius,
                bottomEnd = 0.dp,
                bottomStart = 0.dp
            ),
            colors = CardDefaults.elevatedCardColors().copy(
                containerColor = Color.Black
            )
        ) {
            Spacer(Modifier.size(20.dp))
            Text(
                title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 22.sp
            )
            Spacer(Modifier.size(20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(10.dp),
            ) {
                items(sheetOptions) {
                    SheetItem(it)
                }
            }
            Spacer(Modifier.size(20.dp))
        }
    }
}

@Composable
fun SheetItem(sheetOption: SheetOption) {
    Column(
        Modifier
            .clip(CircleShape)
            .clickable { sheetOption.onClick() }
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = sheetOption.icon,
            contentDescription = sheetOption.title,
            tint = colorScheme.surface,
            modifier = Modifier
                .clip(CircleShape)
                .background(color = colorScheme.onSurface)
                .padding(5.dp)
        )
        Spacer(Modifier.size(10.dp))
        Text(
            sheetOption.title,
            color = colorScheme.onSurface,
        )
    }
}
