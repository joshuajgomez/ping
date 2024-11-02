package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.ping.graph.ChatScreen
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChatListState {
    data object Loading : ChatListState()
    data object Empty : ChatListState()
    data class Ready(val chats: List<Chat>) : ChatListState()
}

data class ChatUiState(
    val me: User,
    val you: User?,
    val chatListState: ChatListState,
)

sealed class ChatInlineUiState {
    data object Empty : ChatInlineUiState()
    data class Reply(val chat: Chat, val fromUserName: String) : ChatInlineUiState()
    data class Image(val imageUrl: String) : ChatInlineUiState()
    data class WebUrl(val webUrl: String) : ChatInlineUiState()
}

@HiltViewModel
class ChatViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    currentUserInfo: CurrentUserInfo,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChatUiState(
            currentUserInfo.currentUser,
            null,
            ChatListState.Loading
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        val userId = savedStateHandle.toRoute<ChatScreen>().userId
        viewModelScope.launch {
            _uiState.update {
                it.copy(you = userRepository.getUser(userId))
            }
        }
        listenForChatUpdates(userId)
    }

    private fun listenForChatUpdates(userId: String) {
        chatObserverJob?.cancel()
        chatObserverJob = viewModelScope.launch {
            chatRepository.observeChatsForUserLocal(userId).cancellable()
                .collect { chats ->
                    _uiState.update {
                        it.copy(
                            chatListState = when {
                                chats.isEmpty() -> ChatListState.Empty
                                else -> ChatListState.Ready(chats)
                            }
                        )
                    }
                    chatRepository.updateChatStatusToServer(Chat.READ, chats)
                }
        }
    }

    fun getChatPreviewState(chat: Chat): ChatInlineUiState = with(_uiState.value) {
        when (chatListState) {
            is ChatListState.Ready -> when {
                chat.replyToChatId.isNotEmpty() -> {
                    val replyChat: Chat? = chatListState.chats.firstOrNull {
                        it.docId == chat.replyToChatId
                    }
                    val fromUserName: String = when (replyChat?.fromUserId == you?.docId) {
                        true -> you?.name ?: "Unknown"
                        else -> "You"
                    }
                    when {
                        replyChat != null -> ChatInlineUiState.Reply(
                            chat = replyChat,
                            fromUserName = fromUserName
                        )

                        else -> ChatInlineUiState.Empty
                    }
                }

                chat.webUrl.isNotEmpty() -> {
                    ChatInlineUiState.WebUrl(chat.webUrl)
                }

                chat.imageUrl.isNotEmpty() -> {
                    ChatInlineUiState.Image(chat.imageUrl)
                }

                else -> {
                    ChatInlineUiState.Empty
                }
            }

            else -> {
                Logger.error("chats.isEmpty")
                return ChatInlineUiState.Empty
            }
        }

    }

    fun onScreenExit() {
        Logger.entry()
        chatObserverJob?.cancel()
    }

    companion object {
        private var chatObserverJob: Job? = null
    }
}