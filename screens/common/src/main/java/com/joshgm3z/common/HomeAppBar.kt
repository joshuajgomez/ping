package com.joshgm3z.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
fun PreviewHomeAppBar() {
    PingTheme {
        HomeAppBar()
    }
}

@Composable
fun HomeAppBarContainer(title: String) {
    HomeAppBar(title)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(title: String = "Chats") {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = colorScheme.onSurface,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    )
}
