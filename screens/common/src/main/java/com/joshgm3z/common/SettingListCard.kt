package com.joshgm3z.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.joshgm3z.common.theme.Green50
import com.joshgm3z.common.theme.PingTheme
import com.joshgm3z.common.theme.Red20

@DarkPreview
@Composable
private fun PreviewSettingListCard() {
    PingTheme {
        Box(
            modifier = Modifier.padding(15.dp),
        ) {
            SettingListCard(Setting.samples)
        }
    }
}

data class Setting(
    val title: String = "",
    val subTitle: String = "",
    val icon: ImageVector? = null,
    val enabled: Boolean = true,
    val textColor: Color? = null,
    val action: () -> Unit = {},
) {
    companion object {
        val samples = listOf(
            Setting(title = "Setting title", subTitle = "Setting sub title"),
            Setting(icon = Icons.Default.CameraAlt, title = "Open camera"),
            Setting(icon = Icons.Default.ModeFanOff, title = "Turn off fan"),
            Setting(textColor = Green50),
            Setting(textColor = Red20),
            Setting(icon = Icons.Default.ModeFanOff, textColor = Red20, subTitle = ""),
            Setting(enabled = false),
        )
    }
}

@Composable
fun SettingListCard(
    settingList: List<Setting>,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
        ),
        modifier = modifier
    ) {
        Column {
            settingList.forEachIndexed { index, setting ->
                SettingItem(setting)
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
        setting.textColor != null -> setting.textColor
        setting.enabled -> colorScheme.onSurface
        else -> colorScheme.onSurface.copy(alpha = 0.3f)
    }
    val subColor = when {
        setting.enabled -> colorScheme.onSurface.copy(alpha = 0.7f)
        else -> colorScheme.onSurface.copy(alpha = 0.1f)
    }
    Box(
        modifier = Modifier
            .clickable(setting.enabled) { setting.action() }
            .fillMaxWidth()
            .padding(vertical = 15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = 20.dp,
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
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(setting.title, fontSize = 18.sp, color = color)
                if (setting.subTitle.isNotEmpty()) {
                    Spacer(Modifier.height(3.dp))
                    Text(
                        setting.subTitle,
                        fontSize = 15.sp,
                        color = subColor
                    )
                }
            }
        }
    }
}
