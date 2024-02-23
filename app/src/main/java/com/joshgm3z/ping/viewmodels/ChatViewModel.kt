package com.joshgm3z.ping.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ChatUiState {
    data class Ready(val message: String) : ChatUiState()
    data class Loading(val message: String) : ChatUiState()
}

class ChatViewModel(private val repository: PingRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState.Loading("Fetching messages..."))
    val uiState: StateFlow<ChatUiState> = _uiState

    var user: User? = null
    var chatList: LiveData<List<Chat>> = MutableLiveData()

    fun onSendButtonClick(message: String) {
        val chat = Chat(message = message)
        chat.toUserId = user!!.docId
        chat.sentTime = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            repository.addChat(chat)
        }
    }

    fun fetchUser(userId: String) {
        Logger.debug("userId = [${userId}]")
        viewModelScope.launch {
            Logger.debug("user before = [$user]")
            user = repository.getUser(userId)
            Logger.debug("user after = [$user]")
            chatList = repository.getChatForUser(userId)
        }.invokeOnCompletion {
            Logger.debug("chat screen ready calling")
            _uiState.value = ChatUiState.Ready("chat screen ready")
        }
    }
}