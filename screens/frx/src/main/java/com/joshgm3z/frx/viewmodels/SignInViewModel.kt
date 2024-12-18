package com.joshgm3z.frx.viewmodels

import androidx.lifecycle.ViewModel
import com.joshgm3z.repository.api.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    fun onSignInClick(
        name: String,
        onSignInComplete: (name: String) -> Unit,
        onNewUser: (name: String) -> Unit,
        onError: (message: String) -> Unit,
    ) {
        userRepository.checkUserInServer(
            name,
            onCheckComplete = { onSignInComplete(name) },
            onNotFound = { onNewUser(name) },
            onCheckError = { onError("Unable to connect to server") },
        )
    }
}