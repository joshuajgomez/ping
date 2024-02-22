package com.joshgm3z.ping.ui.chat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.utils.randomUser

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ChatScreen(
    chatListLive: LiveData<List<Chat>> = MutableLiveData(),
    user: User = randomUser(),
    onSendClick:(message:String)->Unit={}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Title(user)
        Surface(modifier = Modifier.weight(1f)) {
            ChatList(chatListLive)
        }
        InputBox(onSendClick = {onSendClick(it)})
    }
}
