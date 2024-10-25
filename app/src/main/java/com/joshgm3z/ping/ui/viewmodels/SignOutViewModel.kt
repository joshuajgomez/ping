package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.repository.api.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

sealed class SignOutUiState {
    data object Initial : SignOutUiState()
    data object Loading : SignOutUiState()
    data object SignedOut : SignOutUiState()
}

@HiltViewModel
class SignOutViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignOutUiState>(SignOutUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun onSignOutClicked(onSignOutComplete: () -> Unit) {
        _uiState.value = SignOutUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.signOutUser()
            delay(3.seconds)
            _uiState.value = SignOutUiState.SignedOut
            delay(1.seconds)
            viewModelScope.launch {
                onSignOutComplete()
            }
        }
    }
}