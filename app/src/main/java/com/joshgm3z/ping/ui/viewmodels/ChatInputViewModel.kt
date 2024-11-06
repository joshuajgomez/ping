package com.joshgm3z.ping.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.data.util.FileType
import com.joshgm3z.data.util.getFileType
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.ImageRepository
import com.joshgm3z.utils.FileUtil
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
    data class WebUrl(val url: String) : ChatInputUiState()
}

@HiltViewModel
class ChatInputViewModel
@Inject constructor(
    currentUserInfo: CurrentUserInfo,
    private val fileUtil: FileUtil,
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
                    newChat.fileName = fileUtil.getFileName(imageUri)
                    newChat.fileSize = fileUtil.getFileSizeString(imageUri)
                    newChat.fileType = fileUtil.getFileTypeString(imageUri)
                    newChat.fileLocalUriToUpload = imageUri.toString()
                }

                is ChatInputUiState.File -> {
                    newChat.fileName = fileUtil.getFileName(fileUri)
                    newChat.fileSize = fileUtil.getFileSizeString(fileUri)
                    newChat.fileType = fileUtil.getFileTypeString(fileUri)
                    newChat.fileLocalUriToUpload = fileUri.toString()
                }

                else -> {}
            }
            viewModelScope.launch {
                Logger.debug("newChat = [$newChat]")
                chatRepository.insertLocal(newChat)
            }
            if (this is ChatInputUiState.Image
                || this is ChatInputUiState.File
            ) {
                uploadChatImage(newChat)
            } else {
                chatRepository.uploadChatWithId(newChat, {}, {})
            }
        }
    }

    private fun uploadChatImage(chat: Chat) {
        val fileName =
            "chat_${chat.fromUserId}_${fileUtil.getFileName(Uri.parse(chat.fileLocalUriToUpload))}"
        Logger.debug("fileName = [$fileName]")
        imageRepository.uploadFile(
            folderName = FirestoreKey.keyChatFiles,
            fileName = fileName,
            localUri = Uri.parse(chat.fileLocalUriToUpload),
            onProgress = { progress ->
                chat.fileUploadProgress = progress
                viewModelScope.launch {
                    chatRepository.updateProgress(chat.docId, progress)
                }
            },
            onSuccess = {
                chat.fileLocalUriToUpload = ""
                chat.fileUploadProgress = 0f
                viewModelScope.launch {
                    chatRepository.updateChatLocal(chat)
                }
                chat.fileOnlineUrl = it
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

    fun updatePreviewStateWithFile(uri: Uri) {
        when (getFileType(fileUtil.getFileTypeString(uri))) {
            FileType.Image -> {
                _uiState.value = ChatInputUiState.Image(uri)
            }

            else -> {
                _uiState.value = ChatInputUiState.File(uri)
            }
        }

    }
}