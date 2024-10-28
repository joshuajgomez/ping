package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.HomeChat
import com.joshgm3z.data.model.User
import com.joshgm3z.ping.utils.DataUtil
import com.joshgm3z.ping.utils.prettyTime
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
        val homeChats: List<HomeChat>,
        val messages: List<Message>,
        val users: List<User>,
    ) : AllSearchUiState()

    data class SearchEmpty(
        val message: String = "Sorry I couldn't find anything"
    ) : AllSearchUiState()
}

data class Message(
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
    private val currentUserInfo: CurrentUserInfo,
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val dataUtil: DataUtil,
) : ViewModel() {

    private val me: User
        get() = currentUserInfo.currentUser

    private val _uiState = MutableStateFlow<AllSearchUiState>(AllSearchUiState.Initial())
    val uiState = _uiState.asStateFlow()

    private val _queryFlow = MutableStateFlow("")
    val queryFlow = _queryFlow.asStateFlow()

    fun onSearchTextInput(query: String) {
        Logger.debug("query = [${query}]")
        _queryFlow.value = query
        if (query.isEmpty()) {
            _uiState.value = AllSearchUiState.Initial()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val chats = chatRepository.searchChat(query)
            val messages = chats.map { it ->
                Logger.debug("it = [$it]")
                val name = when (me.docId) {
                    it.fromUserId -> it.toUserId
                    else -> it.fromUserId
                }.let {
                    userRepository.getUser(it).name
                }
                Message(
                    message = it.message,
                    name = name,
                    sentTime = it.sentTime.prettyTime(),
                    chatId = it.docId,
                    otherGuyId = it.fromUserId,
                    isOutwards = it.isOutwards
                )
            }
            val users = userRepository.searchUsers(query)

            val homeChats = dataUtil.buildHomeChats(me.docId, chats, users)
            val otherUsers = getRemainingUsers(users, homeChats)

            _uiState.value = when {
                chats.isEmpty() && users.isEmpty() -> AllSearchUiState.SearchEmpty()
                else -> AllSearchUiState.SearchResult(
                    query,
                    homeChats,
                    messages,
                    otherUsers,
                )
            }
        }
    }

    private fun getRemainingUsers(
        users: List<User>,
        homeChats: List<HomeChat>
    ): List<User> {
        val userIds = homeChats.map { it.otherGuy.docId }
        return users.filter {
            !userIds.contains(it.docId)
        }
    }
}
