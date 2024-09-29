package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.R
import com.joshgm3z.repository.PingRepository
import com.joshgm3z.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val repository: com.joshgm3z.repository.PingRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UsersUiState> =
        MutableStateFlow(UsersUiState.Ready(emptyList()))
    val uiState: StateFlow<UsersUiState> = _uiState

    private lateinit var users: List<User>
    lateinit var me: User

    init {
        refreshUserList()
    }

    fun refreshUserList() {
        if (!repository.isUserSignedIn()) {
            com.joshgm3z.utils.Logger.warn("user not signed in")
            return
        }
        com.joshgm3z.utils.Logger.entry()
        viewModelScope.launch {
            repository.syncUserListWithServer {
                viewModelScope.launch {
                    users = repository.getUsers()
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

    fun onImageConfirmed(imageRes: Int) {
        viewModelScope.launch {
            repository.updateUserImageToServer(imageRes)
        }
    }

    fun updateCurrentUser() {
        viewModelScope.launch {
            me = repository.getCurrentUser()
        }
    }

    fun getImageRes(): Int {
        return if (me.imagePath.isNotEmpty() && me.imagePath != "null") {
            me.imagePath.toInt()
        } else R.drawable.default_user
    }

    fun onSignOutClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.signOutUser()
        }
    }

}