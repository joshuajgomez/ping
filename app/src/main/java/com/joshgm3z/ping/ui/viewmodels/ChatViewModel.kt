package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.User
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.ui.PingNavState
import com.joshgm3z.ping.ui.navChat
import com.joshgm3z.ping.utils.DataUtil
import com.joshgm3z.ping.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch

sealed class ChatUiState {
    data class Ready(val you: User, val chats: List<Chat>) : ChatUiState()
    data class Loading(val message: String) : ChatUiState()
}

class ChatViewModel(
    private val repository: PingRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState.Loading("Fetching messages"))
    val uiState: StateFlow<ChatUiState> = _uiState

    private lateinit var otherGuy: User
    private var me: User? = null

    fun onSendButtonClick(message: String) {
        Logger.debug("message = [${message}]")
        val chat = Chat(message = message)
        chat.toUserId = otherGuy.docId
        chat.fromUserId = me!!.docId
        chat.sentTime = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            repository.addChat(chat)
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
            Logger.debug("setUser otherGuy = [${otherGuy}]")
            repository.getChatsOfUserForChatScreen(userId = otherGuy.docId).cancellable()
                .collect {
                    Logger.debug("collect.user = [${otherGuy}], chats = [$it]")
                    val chats = DataUtil.markOutwardChats(me!!.docId, ArrayList(it))
                    _uiState.value = ChatUiState.Ready(otherGuy, chats)
                    repository.updateChatStatus(Chat.READ, chats)
                }
        }
    }

    fun onScreenExit() {
        Logger.entry()
        chatObserverJob?.cancel()
    }
}