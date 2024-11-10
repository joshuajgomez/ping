package com.joshgm3z.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.common.navigation.AllSearch
import com.joshgm3z.common.navigation.SettingRoute
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
fun PreviewHomeAppBar() {
    PingTheme {
        HomeAppBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(navigateTo: (Any) -> Unit = {}) {
    MediumTopAppBar(
        title = {
            Text(
                text = "Chats",
                fontSize = 25.sp,
                modifier = Modifier.padding(start = 10.dp),
            )
        },
        actions = {
            TopBarIcon(icon = Icons.Default.Search) { navigateTo(AllSearch) }
            Spacer(Modifier.size(10.dp))
            TopBarIcon(icon = Icons.Default.Settings) { navigateTo(SettingRoute) }
        }
    )
}
