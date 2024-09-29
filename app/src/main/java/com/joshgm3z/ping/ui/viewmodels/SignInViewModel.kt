package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.repository.PingRepository
import com.joshgm3z.data.model.User
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
    private val repository: com.joshgm3z.repository.PingRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<SignInUiState> =
        MutableStateFlow(SignInUiState.SignIn("Enter your name"))
    val uiState: StateFlow<SignInUiState> = _uiState

    lateinit var currentUser: User

    init {
        setCurrentUser()
    }

    private fun setCurrentUser() {
        com.joshgm3z.utils.Logger.entry()
        viewModelScope.launch {
            if (repository.isUserSignedIn()) {
                currentUser = repository.getCurrentUser()
                com.joshgm3z.utils.Logger.debug("currentUser = [$currentUser]")
            }
        }
    }

    fun onSignInClick(
        name: String,
        onSignInComplete: () -> Unit,
    ) {
        _uiState.value = SignInUiState.Loading("Finding you")
        repository.checkUserInServer(name) {
            if (it == null) {
                // new user, proceed to sign up
                _uiState.value = SignInUiState.SignUp(name)
            } else {
                // user found, proceed to home screen
                setCurrentUser()
                onSignInComplete()
            }
        }
    }

    fun onSignUpClick(
        name: String,
        imagePath: String,
        onSignUpComplete: () -> Unit,
    ) {
        _uiState.value = SignInUiState.Loading("Creating your profile")
        repository.createUserInServer(name, imagePath) { isSuccess, message ->
            if (isSuccess) {
                setCurrentUser()
                onSignUpComplete()
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