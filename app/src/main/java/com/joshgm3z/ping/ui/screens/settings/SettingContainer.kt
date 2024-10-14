package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.common.DarkPreview
import com.joshgm3z.ping.ui.theme.PingTheme

@DarkPreview
@Composable
private fun SettingContainerPreview() {
    PingTheme {
        SettingContainer("Sample setting") {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Turn on something")
                Text("End of setting")
            }
        }
    }
}

@Composable
fun SettingContainer(
    title: String,
    isCloseEnabled: Boolean = true,
    onCloseClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SettingTitle(title, isCloseEnabled, onCloseClick)
        Column(Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
            content()
        }
    }
}

@Composable
private fun SettingTitle(
    title: String,
    isCloseEnabled: Boolean,
    onCloseClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface
        )
        IconButton(
            onCloseClick,
            enabled = isCloseEnabled
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = colorScheme.onSurface
            )
        }
    }
}
