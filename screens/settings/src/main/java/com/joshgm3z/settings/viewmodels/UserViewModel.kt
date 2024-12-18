package com.joshgm3z.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.User
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UsersUiState {
    data class Ready(val users: List<User>) : UsersUiState()
}

@HiltViewModel
class UserViewModel
@Inject constructor(
    private val userRepository: UserRepository,
    private val currentUserInfo: CurrentUserInfo,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UsersUiState> =
        MutableStateFlow(UsersUiState.Ready(emptyList()))
    val uiState: StateFlow<UsersUiState> = _uiState

    private lateinit var users: List<User>

    val me: User
        get() = currentUserInfo.currentUser

    init {
        refreshUserList()
    }

    private fun refreshUserList() {
        if (!currentUserInfo.isSignedIn) {
            Logger.warn("user not signed in")
            return
        }
        Logger.entry()
        viewModelScope.launch {
            userRepository.syncUserListWithServer {
                viewModelScope.launch {
                    users = userRepository.getUsers()
                }.invokeOnCompletion {
                    if (users.isEmpty()) {
                        _uiState.value = UsersUiState.Ready(emptyList())
                    } else {
                        _uiState.value = UsersUiState.Ready(users)
                    }
                }
            }
        }
    }
}
