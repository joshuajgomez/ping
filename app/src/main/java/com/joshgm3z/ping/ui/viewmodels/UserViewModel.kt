package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.model.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UsersUiState {
    data class Empty(val message: String) : UsersUiState()
    data class Ready(val users: List<User>) : UsersUiState()
}

class UserViewModel(private val repository: PingRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UsersUiState> =
        MutableStateFlow(UsersUiState.Empty("Enter your name"))
    val uiState: StateFlow<UsersUiState> = _uiState

    private lateinit var users: List<User>

    init {
        viewModelScope.launch {
            repository.refreshUserList {
                viewModelScope.launch {
                    users = repository.getUsers()
                }.invokeOnCompletion {
                    if (users.isEmpty()) {
                        _uiState.value = UsersUiState.Empty("No other users")
                    } else {
                        _uiState.value = UsersUiState.Ready(users)
                    }
                }
            }
        }
    }

}