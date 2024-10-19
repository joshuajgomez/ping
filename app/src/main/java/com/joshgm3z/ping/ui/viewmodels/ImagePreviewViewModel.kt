package com.joshgm3z.ping.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.toRoute
import com.joshgm3z.data.model.Chat
import com.joshgm3z.ping.graph.ChatImagePreview
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagePreviewViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val data: ChatImagePreview

    val imageUrl: String
        get() = data.imageUrl

    private val _toName = MutableStateFlow("")
    val toName = _toName.asStateFlow()

    init {
        savedStateHandle.toRoute<ChatImagePreview>().let {
            data = it
            viewModelScope.launch {
                userRepository.getUser(it.toUserId).let {
                    _toName.value = it.name
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
        chat.imageUrl = data.imageUrl
        chat.sentTime = System.currentTimeMillis()
        viewModelScope.launch {
            chatRepository.uploadNewMessage(chat)
        }
    }

}