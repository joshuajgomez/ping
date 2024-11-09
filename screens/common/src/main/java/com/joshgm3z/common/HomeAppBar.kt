package com.joshgm3z.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.common.navigation.AllSearch
import com.joshgm3z.common.navigation.SettingRoute
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
