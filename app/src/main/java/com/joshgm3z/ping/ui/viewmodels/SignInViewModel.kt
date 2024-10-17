package com.joshgm3z.ping.ui.viewmodels

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

sealed class SignInUiState {
    data class Loading(val message: String) : SignInUiState()
    data class SignIn(val message: String) : SignInUiState()
    data class SignUp(val enteredName: String) : SignInUiState()
    data class Error(val message: String) : SignInUiState()
}

@HiltViewModel
class SignInViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SignInUiState> =
        MutableStateFlow(SignInUiState.SignIn("Enter your name"))
    val uiState: StateFlow<SignInUiState> = _uiState

    fun onSignInClick(
        name: String,
        onSignInComplete: (name: String) -> Unit,
        onNewUser: (name: String) -> Unit,
    ) {
        _uiState.value = SignInUiState.Loading("Finding you")
        userRepository.checkUserInServer(name) {
            if (it == null) {
                // new user, proceed to sign up
                onNewUser(name)
            } else {
                // user found, proceed to home screen
                onSignInComplete(name)
            }
        }
    }

    fun onSignUpClick(
        name: String,
        imagePath: String,
        about: String,
        onSignUpComplete: (name: String) -> Unit,
    ) {
        _uiState.value = SignInUiState.Loading("Creating your profile")
        val user = User()
        user.name = name
        user.about = about
        user.imagePath = imagePath
        user.dateOfJoining = System.currentTimeMillis()
        userRepository.createUserInServer(user) { isSuccess, message ->
            if (isSuccess) {
                onSignUpComplete(name)
            } else {
                _uiState.value = SignInUiState.Error("Unable to create user: $message")
            }
        }
    }

    fun onGoToSignInClick() {
        _uiState.value = SignInUiState.SignIn("Enter your name")
    }

    fun resetUiState() {
        _uiState.value = SignInUiState.SignIn("Enter your name")
    }

}