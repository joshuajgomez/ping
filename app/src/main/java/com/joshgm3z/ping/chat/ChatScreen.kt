package com.joshgm3z.ping.chat

import Title
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.joshgm3z.ping.utils.getChatList

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ChatScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Title()
        ChatContainer()
        InputBox()
    }
}

@Composable
fun ChatContainer() {
    LazyColumn(
        reverseLayout = true,
        modifier = Modifier.fillMaxHeight(0.9f)
    ) {
        items(items = getChatList()) {
            ChatItem(chat = it)
        }
    }
}

