package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.User
import com.joshgm3z.ping.utils.prettyTime
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AllSearchUiState {
    data class Initial(
        val message: String = "Search for chats, users or anything"
    ) : AllSearchUiState()

    data class SearchResult(
        val query: String,
        val chats: List<ChatData>,
        val users: List<User>
    ) : AllSearchUiState()

    data class SearchEmpty(
        val message: String = "Sorry I couldn't find anything"
    ) : AllSearchUiState()
}

data class ChatData(
    val message: String = "Whats up my maan",
    val name: String = "Someone",
    val sentTime: String = "Yesterday",
    val chatId: String = "",
    val otherGuyId: String = "",
    val isOutwards: Boolean = false,
)

@HiltViewModel
class AllSearchViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AllSearchUiState>(AllSearchUiState.Initial())
    val uiState = _uiState.asStateFlow()

    fun onSearchTextInput(query: String) {
        viewModelScope.launch {
            val chats = chatRepository.searchChat(query).map {
                ChatData(
                    message = it.message,
                    name = userRepository.getUser(it.fromUserId).name,
                    sentTime = it.sentTime.prettyTime(),
                    chatId = it.docId,
                    otherGuyId = it.fromUserId,
                    isOutwards = it.isOutwards
                )
            }
            val users = userRepository.searchUsers(query)
            _uiState.value = when {
                chats.isEmpty() && users.isEmpty() -> AllSearchUiState.SearchEmpty()
                else -> AllSearchUiState.SearchResult(query, chats, users)
            }
        }
    }
}
