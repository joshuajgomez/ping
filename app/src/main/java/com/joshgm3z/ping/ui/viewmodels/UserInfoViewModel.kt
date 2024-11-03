package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.ping.graph.UserInfo
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserInfoUiState {
    data object Empty : UserInfoUiState()
    data class Ready(val user: User, val mediaChats: List<Chat>) : UserInfoUiState()
}

@HiltViewModel
class UserInfoViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserInfoUiState>(UserInfoUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private val userId: String = savedStateHandle.toRoute<UserInfo>().userId
    private var currentChats: List<Chat> = emptyList()

    init {
        Logger.debug("userId = [$userId]")
        viewModelScope.launch {
            chatRepository.observeChatsForUserLocal(userId)
                .combine(userRepository.getUserFlow(userId)) { chats, user ->
                    Logger.debug("chats = $chats, user = $user")
                    currentChats = chats
                    val mediaChats = chats.filter {
                        it.fileOnlineUrl.isNotEmpty()
                    }
                    UserInfoUiState.Ready(user, mediaChats)
                }.collect {
                    Logger.debug("uiState = $it")
                    it.let { _uiState.value = it }
                }
        }
    }

    fun clearChats(
        onClear: () -> Unit = {},
        onError: () -> Unit = {},
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.clearChats(
                userId,
                currentChats,
                onComplete = {
                    viewModelScope.launch {
                        onClear()
                    }
                },
                onError = {
                    viewModelScope.launch {
                        onError()
                    }
                }
            )
        }
    }

}
