package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ModeFanOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.theme.Green50
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.ui.theme.Red10

@DarkPreview
@Composable
private fun PreviewSettingListCard() {
    PingTheme {
        Box(
            modifier = Modifier.padding(vertical = 15.dp),
        ) {
            SettingListCard(Setting.samples)
        }
    }
}

data class Setting(
    val title: String = "Do something",
    val subTitle: String = "Why we are doing this thing",
    val icon: ImageVector? = null,
    val enabled: Boolean = true,
    val color: Color? = null,
    val action: () -> Unit = {},
) {
    companion object {
        val samples = listOf(
            Setting(),
            Setting(icon = Icons.Default.CameraAlt, title = "Open camera"),
            Setting(icon = Icons.Default.ModeFanOff, title = "Turn off fan"),
            Setting(color = Green50),
            Setting(color = Red10),
            Setting(enabled = false),
        )
    }
}

@Composable
fun SettingListCard(
    settingList: List<Setting>,
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
        ),
    ) {
        LazyColumn {
            itemsIndexed(settingList) { index, item ->
                SettingItem(item)
                if (index < settingList.lastIndex) HorizontalDivider(thickness = 0.5.dp)
            }
        }
    }
}

@Composable
private fun SettingItem(
    setting: Setting,
) {
    val color = when {
        setting.enabled -> colorScheme.onSurface
        else -> colorScheme.onSurface.copy(alpha = 0.3f)
    }
    val subColor = when {
        setting.enabled -> colorScheme.onSurface.copy(alpha = 0.7f)
        else -> colorScheme.onSurface.copy(alpha = 0.1f)
    }
    val bgColor = when {
        setting.color != null && setting.enabled -> setting.color
        else -> Color.Transparent
    }
    Box(
        modifier = Modifier
            .clickable(setting.enabled) { setting.action() }
            .fillMaxWidth()
            .height(75.dp)
            .background(bgColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 10.dp
            )
        ) {
            setting.icon?.let {
                Icon(
                    it,
                    contentDescription = null,
                    tint = color
                )
                Spacer(Modifier.width(20.dp))
            }
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                Text(setting.title, fontSize = 18.sp, color = color)
                Spacer(Modifier.height(5.dp))
                Text(
                    setting.subTitle,
                    fontSize = 15.sp,
                    color = subColor
                )
            }
        }
    }
}
