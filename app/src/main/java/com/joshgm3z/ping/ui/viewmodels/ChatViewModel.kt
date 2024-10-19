package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.ping.graph.ChatScreen
import com.joshgm3z.ping.utils.DataUtil
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChatUiState {
    data class Ready(
        val me: User,
        val you: User,
        val chats: List<Chat>
    ) : ChatUiState()

    data class Loading(val message: String) : ChatUiState()
}

@HiltViewModel
class ChatViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val currentUserInfo: CurrentUserInfo,
    private val dataUtil: DataUtil,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState.Loading("Fetching messages"))
    val uiState: StateFlow<ChatUiState> = _uiState

    private lateinit var otherGuy: User

    private val me: User
        get() = currentUserInfo.currentUser

    init {
        val userId = savedStateHandle.toRoute<ChatScreen>().userId
        setUser(userId)
    }

    fun onSendButtonClick(
        message: String = "",
    ) {
        Logger.debug("message = [${message}]")
        val chat = Chat(message = message)
        chat.toUserId = otherGuy.docId
        chat.fromUserId = me!!.docId
        chat.sentTime = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.uploadNewMessage(chat)
        }
    }

    companion object {
        private var chatObserverJob: Job? = null
    }

    private fun setUser(userId: String) {
        chatObserverJob?.cancel()
        chatObserverJob = viewModelScope.launch {
            otherGuy = userRepository.getUser(userId)
            Logger.debug("setUser otherGuy = [${otherGuy}]")
            chatRepository.observeChatsForUserLocal(userId = otherGuy.docId).cancellable()
                .collect {
                    Logger.debug("collect.user = [${otherGuy}], chats = [$it]")
//                    val chats = dataUtil.markOutwardChats(me.docId, ArrayList(it))
                    _uiState.value = ChatUiState.Ready(me, otherGuy, it)
                    chatRepository.updateChatStatusToServer(Chat.READ, it)
                }
        }
    }

    fun onScreenExit() {
        Logger.entry()
        chatObserverJob?.cancel()
    }

}