package com.joshgm3z.ping.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.ui.theme.Gray40
import com.joshgm3z.ping.ui.theme.Purple10
import com.joshgm3z.ping.ui.theme.Purple20
import com.joshgm3z.ping.ui.theme.Purple40
import com.joshgm3z.ping.ui.theme.Purple60
import com.joshgm3z.ping.utils.DataStoreUtil
import com.joshgm3z.ping.utils.getChatList
import com.joshgm3z.ping.utils.getPrettyTime

@Composable
fun ChatList(chats: List<Chat> = emptyList()) {
    LazyColumn(reverseLayout = true) {
        items(items = chats) {
            ChatItem(chat = it)
        }
    }
}

@Preview
@Composable
fun PreviewIncomingChat() {
    val chat = Chat.random()
    chat.isOutwards = false
    ChatItem(chat)
}

@Preview
@Composable
fun PreviewOutgoingChat() {
    val chat = Chat.random()
    chat.isOutwards = true
    ChatItem(chat)
}

@Composable
fun ChatItem(chat: Chat = Chat.random()) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = if (chat.isOutwards) Alignment.End else Alignment.Start
    ) {
        Box(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (chat.isOutwards) Purple40 else Gray40)
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .widthIn(min = 5.dp, max = 250.dp)
        ) {
            Text(
                text = chat.message,
                fontSize = 18.sp,
                color = if (chat.isOutwards) Purple20 else Color.DarkGray
            )
        }
        Text(
            text = getPrettyTime(chat.sentTime),
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewChatList() {
    LazyColumn(reverseLayout = true, modifier = Modifier.background(Color.Transparent)) {
        items(items = getChatList()) {
            ChatItem(chat = it)
        }
    }
}
