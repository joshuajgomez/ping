package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.User
import com.joshgm3z.ping.MainThread
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
    private val currentUserInfo: CurrentUserInfo,
    @MainThread private val mainScope: CoroutineScope,
) : ViewModel() {

    fun onSignUpClick(
        name: String,
        imagePath: String,
        about: String,
        onSignUpComplete: (name: String) -> Unit,
        onSignUpError: (message: String) -> Unit,
    ) {
        val user = User()
        user.name = name
        user.about = about
        user.imagePath = imagePath
        user.dateOfJoining = System.currentTimeMillis()
        userRepository.createUserInServer(user,
            registerComplete = {
                mainScope.launch {
                    onSignUpComplete(it)
                }
            },
            onError = {
                mainScope.launch {
                    onSignUpError(it)
                }
            })

    }

    fun refreshUserList() {
        if (!currentUserInfo.isSignedIn) {
            Logger.warn("user not signed in")
            return
        }
        Logger.entry()
        viewModelScope.launch {
            userRepository.syncUserListWithServer {

            }
        }
    }

}