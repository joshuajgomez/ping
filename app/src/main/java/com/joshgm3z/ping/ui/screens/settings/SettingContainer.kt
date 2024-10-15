package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
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
                Text("Turn on something", color = colorScheme.onSurface)
                Text("End of setting", color = colorScheme.onSurface)
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
        modifier = Modifier.fillMaxSize().padding(10.dp)
    ) {
        SettingTitle(title, isCloseEnabled, onCloseClick)
        Column(Modifier.padding(vertical = 20.dp, horizontal = 10.dp)) {
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
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
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
        Spacer(Modifier.height(30.dp))
        Text(
            title,
            fontSize = 35.sp,
            color = colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}
