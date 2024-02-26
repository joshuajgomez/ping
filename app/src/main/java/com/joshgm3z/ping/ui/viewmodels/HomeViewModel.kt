package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.model.data.HomeChat
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.utils.DataStoreUtil
import com.joshgm3z.ping.utils.DataUtil
import com.joshgm3z.ping.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    data class Ready(val homeChats: List<HomeChat>) : HomeUiState()
    data class Empty(val message: String) : HomeUiState()
}

class HomeViewModel(
    private val pingRepository: PingRepository,
    private val dataStoreUtil: DataStoreUtil,
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> =
        MutableStateFlow(HomeUiState.Empty(""))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            val me = dataStoreUtil.getCurrentUser()
            val users = pingRepository.getUsers()
            pingRepository.getChatsOfUserForHome(me.docId).collect {
                val homeChats = DataUtil.buildHomeChats(me.docId, it, users)
                Logger.debug("homeChats = [$homeChats]")
                if (homeChats.isNotEmpty()) {
                    _uiState.value = HomeUiState.Ready(homeChats)
                }
            }
        }.invokeOnCompletion {
        }
    }
}
