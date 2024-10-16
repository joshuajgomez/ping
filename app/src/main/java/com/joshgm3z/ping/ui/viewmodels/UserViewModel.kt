package com.joshgm3z.ping.ui.viewmodels

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

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

    fun onSignOutClicked(onSignOutComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.signOutUser()
            delay(3.seconds)
            viewModelScope.launch {
                onSignOutComplete()
            }
        }
    }

    fun saveImage(
        imageUrl: String,
        onImageSaved: () -> Unit,
        onProgress: (progress: Float) -> Unit,
        onFailure: () -> Unit,
    ) {
        Logger.debug("imageUrl = [${imageUrl}]")
        viewModelScope.launch {
            userRepository.uploadImage(
                imageUrl,
                onProgress,
                onImageSaved,
                onFailure
            )
        }
    }

    fun getUser(userId: String) = flow {
        emit(userRepository.getUser(userId))
    }

}
