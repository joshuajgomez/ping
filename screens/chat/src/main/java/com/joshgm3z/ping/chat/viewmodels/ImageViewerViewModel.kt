package com.joshgm3z.ping.chat.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joshgm3z.common.navigation.ChatImageViewer
import com.joshgm3z.data.model.Chat
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    chatRepository: ChatRepository,
    userRepository: UserRepository,
) : ViewModel() {

    private val _chat = MutableStateFlow<Chat?>(null)
    val chatFlow = _chat.asStateFlow()

    var name: String = ""

    init {
        savedStateHandle.toRoute<ChatImageViewer>().let {
            viewModelScope.launch {
                val chat = chatRepository.getChat(it.chatId)
                name = when {
                    chat.isOutwards -> "You"
                    else -> userRepository.getUser(chat.fromUserId)?.name ?: "Unknown"
                }
                _chat.value = chat
            }
        }
    }
}
