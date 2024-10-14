package com.joshgm3z.ping.ui.viewmodels

import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.R
import com.joshgm3z.data.model.User
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.FileUtil
import com.joshgm3z.utils.Logger
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
    private val userRepository: UserRepository,
    private val currentUserInfo: CurrentUserInfo,
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

    fun onImageConfirmed(imageRes: Int) {
        viewModelScope.launch {
            userRepository.updateUserImageToServer(imageRes)
        }
    }

    fun updateCurrentUser() {
        viewModelScope.launch {
            me = currentUserInfo.currentUser
        }
    }

    fun getImageRes(): Int {
        return if (me.imagePath.isNotEmpty() && me.imagePath != "null") {
            me.imagePath.toInt()
        } else R.drawable.default_user
    }

    fun onSignOutClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.signOutUser()
        }
    }

    fun saveImage(imageUri: Uri) {
        Logger.debug("imageUri = [${imageUri}]")
        viewModelScope.launch {
            userRepository.uploadImage(imageUri)
        }
    }

    fun saveIcon(selectedIcon: Int) {
        TODO("Not yet implemented")
    }

}