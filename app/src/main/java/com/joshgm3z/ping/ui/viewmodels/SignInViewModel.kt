package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.model.data.User
import com.joshgm3z.ping.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SignInUiState {
    data class Loading(val message: String) : SignInUiState()
    data class SignIn(val message: String) : SignInUiState()
    data class SignUp(val enteredName: String) : SignInUiState()
    data class Error(val message: String) : SignInUiState()
}

class SignInViewModel(private val repository: PingRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<SignInUiState> =
        MutableStateFlow(SignInUiState.SignIn("Enter your name"))
    val uiState: StateFlow<SignInUiState> = _uiState

    var currentUser: User? = null

    init {
        setCurrentUser()
    }

    private fun setCurrentUser() {
        Logger.entry()
        viewModelScope.launch {
            currentUser = repository.getCurrentUser()
            Logger.debug("currentUser = [$currentUser]")
        }
    }

    fun onSignInClick(
        name: String,
        onSignInComplete: () -> Unit,
    ) {
        _uiState.value = SignInUiState.Loading("Finding you")
        repository.checkUser(name) {
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
        repository.registerUser(name, imagePath) { isSuccess, message ->
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

    fun onSignOutClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.signOutUser()
        }
    }

    fun resetUiState() {
        _uiState.value = SignInUiState.SignIn("Enter your name")
    }

}