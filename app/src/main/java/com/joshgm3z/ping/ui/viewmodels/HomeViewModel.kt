package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.HomeChat
import com.joshgm3z.data.util.getHomeChatList
import com.joshgm3z.ping.utils.DataUtil
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeUiState {
    data class Ready(val homeChats: List<HomeChat> = getHomeChatList()) : HomeUiState()
    data class Empty(val message: String = "Looking for chats") : HomeUiState()
}

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val currentUserInfo: CurrentUserInfo,
    private val dataUtil: DataUtil,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Empty())
    val uiState = _uiState.asStateFlow()

    init {
        startListeningToChats()
    }

    private fun startListeningToChats() {
        if (!currentUserInfo.isSignedIn) {
            Logger.warn("user not signed in")
            return
        }
        viewModelScope.launch {
            val me = currentUserInfo.currentUser
            val users = userRepository.getUsers()
            chatRepository.observeChatsForUserHomeLocal().collectLatest {
                if (it.isNotEmpty() && users.isEmpty()) {
                    Logger.warn("users list not fetched")
                    _uiState.value = HomeUiState.Empty("Fetching users")
                }
                val homeChats = dataUtil.buildHomeChats(me.docId, it, users)
                _uiState.value = HomeUiState.Ready(homeChats)
            }
        }
    }

}
