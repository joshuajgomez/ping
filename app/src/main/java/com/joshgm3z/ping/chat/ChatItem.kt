package com.joshgm3z.ping.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.ui.theme.Green40
import com.joshgm3z.ping.ui.theme.Pink40
import com.joshgm3z.ping.ui.theme.Purple40
import com.joshgm3z.ping.ui.theme.Purple80
import com.joshgm3z.ping.ui.theme.PurpleGrey40
import com.joshgm3z.ping.utils.getChatList

@Composable
fun ChatItem(chat: Chat) {
    val isOutwards = chat.fromUser == null
    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize(),
        horizontalArrangement = if (isOutwards) Arrangement.End else Arrangement.Start
    ) {
        Box(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isOutwards) Purple40 else Green40)
                .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
        ) {
            Text(text = chat.message, color = Color.White, fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewChat() {
    LazyColumn(reverseLayout = true) {
        items(items = getChatList()) {
            ChatItem(chat = it)
        }
    }
}
