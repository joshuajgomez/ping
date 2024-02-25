package com.joshgm3z.ping.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.User
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.ui.PingNavState
import com.joshgm3z.ping.ui.navChat
import com.joshgm3z.ping.utils.DataStoreUtil
import com.joshgm3z.ping.utils.DataUtil
import com.joshgm3z.ping.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ChatUiState {
    data class Ready(val you: User, val chats: List<Chat>) : ChatUiState()
    data class Loading(val message: String) : ChatUiState()
}

class ChatViewModel(
    private val repository: PingRepository,
    private val dataStoreUtil: DataStoreUtil,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState.Loading("Fetching messages"))
    val uiState: StateFlow<ChatUiState> = _uiState

    private lateinit var otherGuy: User
    private lateinit var me: User

    fun onSendButtonClick(message: String) {
        Logger.debug("message = [${message}]")
        val chat = Chat(message = message)
        chat.toUserId = otherGuy.docId
        chat.fromUserId = me.docId
        chat.sentTime = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            repository.addChat(chat)
        }
    }

    fun setUser(userId: String) {
        viewModelScope.launch {
            otherGuy = repository.getUser(userId)
            me = dataStoreUtil.getCurrentUser()
            repository.getChatsOfUser(userId = otherGuy.docId).collect {
                if (PingNavState.currentRoute == navChat) {
                    val chats = DataUtil.markOutwardChats(me.docId, ArrayList(it))
                    _uiState.value = ChatUiState.Ready(otherGuy, chats)
                    repository.updateChatStatus(Chat.READ, chats)
                }
            }
        }.invokeOnCompletion {
            _uiState.value = ChatUiState.Ready(otherGuy, emptyList())
        }
    }
}