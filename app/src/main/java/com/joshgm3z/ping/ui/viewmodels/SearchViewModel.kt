package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.model.data.User
import com.joshgm3z.ping.model.PingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SearchUiState {
    data class Empty(val message: String) : SearchUiState()
    data class Ready(val users: List<User>) : SearchUiState()
    data class SearchResult(val users: List<User>) : SearchUiState()
}

class SearchViewModel(private val pingRepository: PingRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<SearchUiState> =
        MutableStateFlow(SearchUiState.Empty("Fetching other users"))
    val uiState: StateFlow<SearchUiState> = _uiState

    private lateinit var users: List<User>

    init {
        viewModelScope.launch {
            pingRepository.refreshUserList {
                viewModelScope.launch {
                    users = pingRepository.getUsers()
                }.invokeOnCompletion {
                    if (users.isEmpty()) {
                        _uiState.value = SearchUiState.Empty("No other users")
                    } else {
                        _uiState.value = SearchUiState.Ready(users)
                    }
                }
            }
        }
    }
}
