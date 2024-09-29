package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.HomeChat
import com.joshgm3z.repository.PingRepository
import com.joshgm3z.ping.utils.DataUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeUiState {
    data class Ready(val homeChats: List<HomeChat>) : HomeUiState()
    data class Empty(val message: String) : HomeUiState()
}

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val pingRepository: com.joshgm3z.repository.PingRepository,
    private val dataUtil: DataUtil,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Ready(emptyList()))
    val uiState: StateFlow<HomeUiState> = _uiState

    var appTitle: MutableStateFlow<String> = MutableStateFlow("Ping")

    init {
        startListeningToChats()
    }

    fun startListeningToChats() {
        if (!pingRepository.isUserSignedIn()) {
            com.joshgm3z.utils.Logger.warn("user not signed in")
            return
        }
        viewModelScope.launch {
            val me = pingRepository.getCurrentUser()
            val users = pingRepository.getUsers()
            pingRepository.observeChatsForUserHomeLocal(me.docId).collect {
                com.joshgm3z.utils.Logger.debug("home chat list update")
                if (it.isNotEmpty() && users.isEmpty()) {
                    com.joshgm3z.utils.Logger.warn("users list not fetched")
                    _uiState.value = HomeUiState.Empty("Fetching users")
                }
                val homeChats = dataUtil.buildHomeChats(me.docId, it, users)
                _uiState.value = HomeUiState.Ready(homeChats)
            }
        }
    }

    fun setAppTitle(title: String) {
        appTitle.value = title
    }
}
