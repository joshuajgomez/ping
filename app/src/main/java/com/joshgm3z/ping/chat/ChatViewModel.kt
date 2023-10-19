package com.joshgm3z.ping.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.utils.randomUser
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: PingRepository): ViewModel() {

    val user: User = randomUser()
    val chatList: LiveData<List<Chat>> = repository.getChatForUser(user.id)

    fun onSendButtonClick(message:String){
        val chat = Chat("", message = message, System.currentTimeMillis(), null, user.id)
        viewModelScope.launch {
        repository.addChat(chat)
        }
    }
}