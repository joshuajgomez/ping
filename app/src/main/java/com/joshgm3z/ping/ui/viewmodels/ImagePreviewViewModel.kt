package com.joshgm3z.ping.ui.viewmodels

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

    var toName: String? = null

    init {
        savedStateHandle.toRoute<ChatImagePreview>().let {
            data = it
            viewModelScope.launch {
                userRepository.getUser(it.toUserId).let {
                    toName = it.name
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