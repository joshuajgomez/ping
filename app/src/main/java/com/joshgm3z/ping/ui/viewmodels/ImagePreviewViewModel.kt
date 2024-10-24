package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joshgm3z.data.model.Chat
import com.joshgm3z.ping.graph.ChatImagePreview
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.ImageRepository
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ImagePreviewUiState {
    data object Initial : ImagePreviewUiState()

    data class Ready(
        val imageUrl: String,
        val toName: String
    ) : ImagePreviewUiState()

    data class Sending(
        val imageUrl: String,
    ) : ImagePreviewUiState()

    data object Sent : ImagePreviewUiState()
}

@HiltViewModel
class ImagePreviewViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    private val data: ChatImagePreview

    private val _uiState = MutableStateFlow<ImagePreviewUiState>(
        ImagePreviewUiState.Initial
    )
    val uiState = _uiState.asStateFlow()

    init {
        savedStateHandle.toRoute<ChatImagePreview>().let {
            data = it
            viewModelScope.launch {
                userRepository.getUser(it.toUserId).let {
                    _uiState.value = ImagePreviewUiState.Ready(
                        data.imageUrl,
                        it.name
                    )
                }
            }
        }
    }

    fun onSendButtonClick(
        message: String = "",
    ) {
        Logger.debug("message = [${message}]")
        val chat = Chat(message = message)
        chat.toUserId = data.toUserId
        chat.fromUserId = data.myUserId
        chat.sentTime = System.currentTimeMillis()
        _uiState.value = ImagePreviewUiState.Sending(
            data.imageUrl
        )
        viewModelScope.launch {
            imageRepository.uploadImage(
                "chat_${chat.fromUserId}_${System.currentTimeMillis()}",
                data.imageUrl,
                onSuccess = {
                    chat.imageUrl = it
                    chatRepository.uploadChat(
                        chat,
                        onUploaded = {
                            _uiState.value = ImagePreviewUiState.Sent
                        },
                    )
                },
                onProgress = {
                    _uiState.value = ImagePreviewUiState.Sending(
                        data.imageUrl
                    )
                },
                onError = {
                    Logger.debug("onError")
                },
            )
        }
    }

}