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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AllSearchUiState(
    val homeChats: List<HomeChat> = emptyList(),
    val messages: List<Message> = emptyList(),
    val users: List<User> = emptyList(),
)

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

    private val _uiState = MutableStateFlow(AllSearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _queryFlow = MutableStateFlow("")
    val queryFlow = _queryFlow.asStateFlow()

    fun onSearchTextInput(query: String) {
        Logger.debug("query = [${query}]")
        _queryFlow.value = query
        if (query.isEmpty()) {
            _uiState.value = AllSearchUiState()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            checkChats(query)
            val users = userRepository.searchUsers(query)
            getHomeChats(users) { homeChats ->
                _uiState.update {
                    it.copy(homeChats = homeChats)
                }
                _uiState.update {
                    it.copy(users = getRemainingUsers(users, homeChats))
                }
            }
        }
    }

    private suspend fun checkChats(query: String) =
        chatRepository.searchChat(query)
            .map { it ->
                Logger.debug("it = [$it]")
                val name = when (me.docId) {
                    it.fromUserId -> it.toUserId
                    else -> it.fromUserId
                }.let {
                    userRepository.getUser(it)?.name ?: "Unknown"
                }
                Message(
                    message = it.message,
                    name = name,
                    sentTime = it.sentTime.prettyTime(),
                    chatId = it.docId,
                    otherGuyId = it.fromUserId,
                    isOutwards = it.isOutwards
                )
            }.let { messages ->
                _uiState.update {
                    it.copy(messages = messages)
                }
            }

    private fun getHomeChats(
        users: List<User>,
        onFetched: (List<HomeChat>) -> Unit
    ) = viewModelScope.launch {
        val chats = chatRepository.observeChatsForUserHomeLocal().first()
        if (users.isEmpty()) {
            Logger.warn("users list not fetched")
            onFetched(emptyList())
        } else {
            val homeChats = dataUtil.buildHomeChats(me.docId, chats, users)
            onFetched(homeChats)
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

    companion object {
        fun getChatDataList(): List<Message> = listOf(
            Message(isOutwards = true),
            Message(isOutwards = true),
            Message(message = "Some very long Whattt message that is sooooooo long you cant even read it in full long you cant even read it in full "),
            Message(),
            Message(),
            Message(isOutwards = true, message = "Whats"),
            Message(isOutwards = true, message = "What"),
        )
    }
}
