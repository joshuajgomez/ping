package com.joshgm3z.ping.viewmodels

import androidx.lifecycle.ViewModel
import com.joshgm3z.ping.model.PingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class SignInUiState {
    data class Loading(val message: String) : SignInUiState()
    data class SignIn(val message: String) : SignInUiState()
    data class SignUp(val message: String) : SignInUiState()
    data class GoToHome(val message: String) : SignInUiState()
}

class SignInViewModel(private val repository: PingRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<SignInUiState> =
        MutableStateFlow(SignInUiState.SignIn("Enter your name"))
    val uiState: StateFlow<SignInUiState> = _uiState

    fun onSignInClick(name: String) {
        _uiState.value = SignInUiState.Loading("Finding you")
        repository.checkUser(name) {
            if (it == null) {
                // new user, proceed to sign up
                _uiState.value = SignInUiState.Loading("Signing you up")
                repository.registerUser(name) { isSuccess, message ->
                    if (isSuccess) {
                        _uiState.value = SignInUiState.GoToHome("User created succesfully")
                    }
                }
            } else {
                // user found, proceed to home screen
                _uiState.value = SignInUiState.GoToHome("User found")
            }
        }
    }

}