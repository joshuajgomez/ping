package com.joshgm3z.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.common.theme.DarkPreview
import com.joshgm3z.common.theme.PingTheme

@DarkPreview
@Composable
private fun SettingContainerPreview() {
    PingTheme {
        SettingContainer("Sample setting") {
            val settingList = listOf(
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
                Setting("Setting title", "Setting sub title"),
            )
            SettingListCard(settingList)
        }
    }
}

@Composable
fun SettingContainer(
    title: String,
    isCloseEnabled: Boolean = true,
    scrollable: Boolean = true,
    onCloseClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        topBar = { SettingTitle(title, isCloseEnabled, onCloseClick) },
    ) {
        val scrollState = rememberScrollState()
        val modifier = Modifier
        if (scrollable) {
            modifier.verticalScroll(scrollState)
        }
        Column(
            modifier
                .padding(it)
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingTitle(
    title: String,
    isCloseEnabled: Boolean,
    onCloseClick: () -> Unit = {}
) {
    TopAppBar(
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        expandedHeight = 120.dp,
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
            ) {
                IconButton(
                    onCloseClick,
                    enabled = isCloseEnabled,
                    modifier = Modifier
                        .background(
                            colorScheme.onPrimary,
                            shape = CircleShape
                        )
                        .size(40.dp),
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp),
                        tint = when {
                            isCloseEnabled -> colorScheme.primary
                            else -> colorScheme.primary.copy(alpha = 0.5f)
                        }
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    title,
                    fontSize = 32.sp,
                    color = colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
        })

}
