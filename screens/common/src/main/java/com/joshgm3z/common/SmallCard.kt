package com.joshgm3z.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
private fun PreviewSmallCard() {
    PingTheme {
        PingWallpaper {
            SmallCard { Text("Hello world!") }
        }
    }
}

@Composable
fun SmallCard(content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .width(250.dp)
            .height(180.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content()
        }
    }
}