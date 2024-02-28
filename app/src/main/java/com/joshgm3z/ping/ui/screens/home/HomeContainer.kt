package com.joshgm3z.ping.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Groups2
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joshgm3z.ping.ui.theme.PingTheme

@Preview
@Composable
fun PreviewHomeContainer() {
    PingTheme {
        HomeContainer()
    }
}

@Composable
fun HomeContainer() {
    Scaffold(
        topBar = { HomeTitle() },
        bottomBar = { HomeBottomBar() },
    ) {
        HomeChatList(modifier = Modifier.padding(it))
    }
}

@Preview
@Composable
fun HomeBottomBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        NavIcon(imageVector = Icons.Rounded.ChatBubble, isSelected = true)
        NavIcon(imageVector = Icons.Rounded.Groups2)
        NavIcon(imageVector = Icons.Rounded.Settings)
    }
}

@Preview
@Composable
fun NavIcon(
    imageVector: ImageVector = Icons.Rounded.ChatBubble,
    isSelected: Boolean = false,
    onIconClick: () -> Unit = {},
) {
    IconButton(onClick = { onIconClick() }) {
        Icon(
            imageVector = imageVector, contentDescription = null,
            tint = if (isSelected) colorScheme.primaryContainer else colorScheme.outline
        )
    }
}
