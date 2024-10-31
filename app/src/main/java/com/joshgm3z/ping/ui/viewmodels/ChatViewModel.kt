package com.joshgm3z.ping.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.ping.graph.ChatScreen
import com.joshgm3z.ping.ui.screens.chat.InlinePreviewState
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.ImageRepository
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import com.joshgm3z.utils.const.FirestoreKey
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

@HiltViewModel
class ChatViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    currentUserInfo: CurrentUserInfo,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val imageRepository: ImageRepository,
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

    fun onSendButtonClick(
        message: String = "",
        inlinePreviewState: InlinePreviewState,
    ) {
        Logger.debug("message = [${message}]")

        val chat = Chat(message = message)
        with(_uiState.value) {
            chat.docId = chatRepository.createChatDocId()
            chat.toUserId = you?.docId ?: ""
            chat.fromUserId = me.docId
            chat.fromUserName = "You"
            chat.isOutwards = true
            chat.sentTime = System.currentTimeMillis()
        }

        when (inlinePreviewState) {
            is InlinePreviewState.Reply -> {
                chat.replyToChatId = inlinePreviewState.chat.docId
            }

            is InlinePreviewState.Image -> {
                chat.imageUploadUri = inlinePreviewState.imageUri.toString()
            }

            is InlinePreviewState.Empty -> {}
            is InlinePreviewState.WebUrl -> {}
        }
        viewModelScope.launch {
            chatRepository.insertLocal(chat)
        }
        if (inlinePreviewState is InlinePreviewState.Image) {
            uploadChatImage(chat)
        } else {
            chatRepository.uploadChatWithId(chat, {}, {})
        }
    }

    fun onScreenExit() {
        Logger.entry()
        chatObserverJob?.cancel()
    }

    private fun uploadChatImage(chat: Chat) {
        val imageFileName = "chat_${chat.fromUserId}_${System.currentTimeMillis()}.jpg"
        imageRepository.uploadImage(
            folderName = FirestoreKey.keyChatImages,
            fileName = imageFileName,
            localUri = Uri.parse(chat.imageUploadUri),
            onProgress = { progress ->
                chat.imageUploadProgress = progress
                viewModelScope.launch {
                    chatRepository.updateChatLocal(chat)
                }
            },
            onSuccess = {
                chat.imageUploadUri = ""
                chat.imageUploadProgress = 0f
                viewModelScope.launch {
                    chatRepository.updateChatLocal(chat)
                }
                chat.imageUrl = it
                chatRepository.uploadChatWithId(chat, {}, {})
            }
        )
    }

    companion object {
        private var chatObserverJob: Job? = null
    }
}