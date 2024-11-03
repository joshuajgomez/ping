package com.joshgm3z.ping.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.ping.ui.screens.chat.FileType
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.ImageRepository
import com.joshgm3z.utils.Logger
import com.joshgm3z.utils.const.FirestoreKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ChatInputUiState {
    data object Empty : ChatInputUiState()
    data class Reply(val chat: Chat, val fromName: String) : ChatInputUiState()
    data class Image(val imageUri: Uri) : ChatInputUiState()
    data class File(val fileUri: Uri) : ChatInputUiState()
    data class Pdf(val fileUri: Uri) : ChatInputUiState()
    data class WebUrl(val url: String) : ChatInputUiState()
}

@HiltViewModel
class ChatInputViewModel
@Inject constructor(
    currentUserInfo: CurrentUserInfo,
    private val chatRepository: ChatRepository,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChatInputUiState>(ChatInputUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private val me: User = currentUserInfo.currentUser
    lateinit var otherGuy: User

    fun onSendButtonClick(
        message: String = "",
    ) {
        val previewState = _uiState.value
        _uiState.value = ChatInputUiState.Empty
        Logger.debug("message = [${message}]")
        val newChat = Chat(message = message).apply {
            docId = chatRepository.createChatDocId()
            toUserId = otherGuy.docId
            fromUserId = me.docId
            isOutwards = true
            sentTime = System.currentTimeMillis()
        }

        with(previewState) {
            when (this) {
                is ChatInputUiState.Reply -> {
                    newChat.replyToChatId = chat.docId
                }

                is ChatInputUiState.Image -> {
                    newChat.imageUploadUri = imageUri.toString()
                }

                else -> {}
            }
            viewModelScope.launch {
                Logger.debug("newChat = [$newChat]")
                chatRepository.insertLocal(newChat)
            }
            if (this is ChatInputUiState.Image) {
                uploadChatImage(newChat)
            } else {
                chatRepository.uploadChatWithId(newChat, {}, {})
            }
        }
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

    fun clearPreviewState() {
        updatePreviewState(ChatInputUiState.Empty)
    }

    private fun updatePreviewState(uiState: ChatInputUiState) {
        Logger.debug("uiState = [${uiState}]")
        _uiState.value = uiState
    }

    fun updateReplyPreviewState(chat: Chat) {
        val fromName = when (chat.fromUserId) {
            me.docId -> "You"
            else -> otherGuy.name
        }
        _uiState.value = ChatInputUiState.Reply(chat, fromName)
    }

    fun updatePreviewStateWithFile(uri: Uri, fileType: FileType) {
        when (fileType) {
            FileType.Image -> {
                _uiState.value = ChatInputUiState.Image(uri)
            }

            else -> {
                _uiState.value = ChatInputUiState.File(uri)
            }
        }

    }
}