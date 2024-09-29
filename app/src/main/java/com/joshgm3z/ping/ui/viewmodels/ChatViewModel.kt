package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.repository.PingRepository
import com.joshgm3z.ping.utils.DataUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChatUiState {
    data class Ready(val you: User, val chats: List<Chat>) : ChatUiState()
    data class Loading(val message: String) : ChatUiState()
}

@HiltViewModel
class ChatViewModel
@Inject constructor(
    private val repository: com.joshgm3z.repository.PingRepository,
    private val dataUtil: DataUtil,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState.Loading("Fetching messages"))
    val uiState: StateFlow<ChatUiState> = _uiState

    private lateinit var otherGuy: User
    private var me: User? = null

    fun onSendButtonClick(message: String) {
        com.joshgm3z.utils.Logger.debug("message = [${message}]")
        val chat = Chat(message = message)
        chat.toUserId = otherGuy.docId
        chat.fromUserId = me!!.docId
        chat.sentTime = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            repository.uploadNewMessage(chat)
        }
    }

    companion object {
        private var chatObserverJob: Job? = null
    }

    fun setUser(userId: String) {
        chatObserverJob?.cancel()
        chatObserverJob = viewModelScope.launch {
            otherGuy = repository.getUser(userId)
            me = repository.getCurrentUser()
            com.joshgm3z.utils.Logger.debug("setUser otherGuy = [${otherGuy}]")
            repository.observeChatsForUserLocal(userId = otherGuy.docId).cancellable()
                .collect {
                    com.joshgm3z.utils.Logger.debug("collect.user = [${otherGuy}], chats = [$it]")
                    val chats = dataUtil.markOutwardChats(me!!.docId, ArrayList(it))
                    _uiState.value = ChatUiState.Ready(otherGuy, chats)
                    repository.updateChatStatusToServer(Chat.READ, chats)
                }
        }
    }

    fun onScreenExit() {
        com.joshgm3z.utils.Logger.entry()
        chatObserverJob?.cancel()
    }
}