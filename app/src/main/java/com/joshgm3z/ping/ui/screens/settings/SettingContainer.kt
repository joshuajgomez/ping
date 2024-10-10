package com.joshgm3z.ping.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.ui.theme.PingTheme

@Preview
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
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp),
    ) {
        SettingTitle(title, isCloseEnabled)
        content()
    }
}

@Composable
private fun SettingTitle(
    title: String,
    isCloseEnabled: Boolean,
    onCloseClick: () -> Unit = {}
) {
    Row(Modifier.height(80.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onCloseClick,
            enabled = isCloseEnabled
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(Modifier.width(20.dp))
        Text(
            title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
