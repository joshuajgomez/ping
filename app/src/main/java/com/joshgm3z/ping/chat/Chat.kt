package com.joshgm3z.ping.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.ui.theme.Gray40
import com.joshgm3z.ping.ui.theme.Green40
import com.joshgm3z.ping.ui.theme.Purple40
import com.joshgm3z.ping.ui.theme.Purple60
import com.joshgm3z.ping.utils.getChatList
import com.joshgm3z.ping.utils.getPrettyTime

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatList() {
    LazyColumn(reverseLayout = true) {
        items(items = getChatList()) {
            ChatItem(chat = it)
        }
    }
}

@Composable
fun ChatItem(chat: Chat) {
    val isOutwards = chat.fromUser == null
    Column(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .fillMaxSize(),
        horizontalAlignment = if (isOutwards) Alignment.End else Alignment.Start
    ) {
        Box(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isOutwards) Purple60 else Gray40)
                .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
        ) {
            Text(
                text = chat.message,
                fontSize = 20.sp,
                color = if (isOutwards) Color.White else Color.Black
            )
        }
        Text(
            text = getPrettyTime(chat.sentTime),
            color = Color.DarkGray,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp))
    }
}
