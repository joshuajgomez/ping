package com.joshgm3z.ping.chat

import Title
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ChatScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Title()
        Surface(modifier = Modifier.weight(1f)) {
            ChatList()
        }
        InputBox(defaultText = "Hello from control")
    }
}
