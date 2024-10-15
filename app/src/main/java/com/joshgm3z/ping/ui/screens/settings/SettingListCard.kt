package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.theme.PingTheme

@DarkPreview
@Composable
fun PreviewSettingListCard() {
    PingTheme {
        Box {
            SettingListCard()
        }
    }
}

data class Setting(
    val title: String = "Do something",
    val subTitle: String = "Why we are doing this thing",
    val icon: ImageVector? = null,
    val action: () -> Unit = {},
) {
    companion object {
        val samples = listOf(
            Setting(),
            Setting(),
            Setting(),
            Setting(),
        )
    }
}

@Composable
fun SettingListCard(
    settingList: List<Setting> = Setting.samples,
) {
    ElevatedCard(
        modifier = Modifier.padding(15.dp),
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = colorScheme.surfaceContainerHigh.copy(alpha = 0.7f)
        )
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
fun SettingItem(
    setting: Setting,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { setting.action() }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(setting.title, fontSize = 18.sp, color = colorScheme.onSurface)
        Spacer(Modifier.height(5.dp))
        Text(setting.subTitle, fontSize = 15.sp, color = colorScheme.onSurface.copy(alpha = 0.7f))
    }
}