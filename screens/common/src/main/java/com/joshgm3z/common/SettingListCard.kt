package com.joshgm3z.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ModeFanOff
import androidx.compose.material.icons.filled.Save
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
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewSettingListCard() {
    PingTheme {
        Box(
            modifier = Modifier.padding(15.dp),
        ) {
            SettingListCard(sampleSettings())
        }
    }
}

@Composable
fun settingGreen(
    title: String,
    subTitle: String = "",
    icon: ImageVector? = null,
    enabled: Boolean = true,
) = Setting(title, subTitle, icon, enabled, colorScheme.primary)

@Composable
fun settingRed(
    title: String,
    subTitle: String = "",
    icon: ImageVector? = null,
    enabled: Boolean = true,
) = Setting(title, subTitle, icon, enabled, colorScheme.error)

open class Setting(
    val title: String = "",
    val subTitle: String = "",
    val icon: ImageVector? = null,
    val enabled: Boolean = true,
    val textColor: Color? = null,
    val action: () -> Unit = {},
)

@Composable
fun sampleSettings() = listOf(
    Setting(title = "Setting title", subTitle = "Setting sub title"),
    Setting(
        icon = Icons.Default.ModeFanOff,
        title = "Setting title",
        subTitle = "Setting sub title"
    ),
    Setting(icon = Icons.Default.CameraAlt, title = "Open camera"),
    Setting(icon = Icons.Default.ModeFanOff, title = "Turn off fan"),
    settingGreen(icon = Icons.Default.Save, title = "Setting title"),
    settingRed(icon = Icons.Default.ModeFanOff, title = "Setting title"),
    Setting(title = "Setting title", enabled = false),
)

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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(setting.enabled) { setting.action() }
            .fillMaxWidth()
            .padding(
                vertical = when {
                    setting.subTitle.isEmpty() -> 15.dp
                    else -> 10.dp
                },
                horizontal = 15.dp
            )
    ) {
        setting.icon?.let {
            Icon(
                it,
                contentDescription = null,
                tint = color
            )
            Spacer(Modifier.width(15.dp))
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(setting.title, color = color)
            if (setting.subTitle.isNotEmpty()) {
                Text(
                    setting.subTitle,
                    fontSize = 13.sp,
                    color = subColor
                )
            }
        }
    }
}
