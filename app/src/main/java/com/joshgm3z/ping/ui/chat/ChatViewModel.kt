package com.joshgm3z.ping.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.utils.randomUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: PingRepository) : ViewModel() {

    val user: User = randomUser()
    val chatList: LiveData<List<Chat>> = repository.getChatForUser(user.docId)

    fun onSendButtonClick(message: String) {
        val chat = Chat(message = message)
        chat.toUserId = user.docId
        chat.sentTime = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            repository.addChat(chat)
        }
    }
}