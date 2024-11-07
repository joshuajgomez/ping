package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.joshgm3z.data.model.User
import com.joshgm3z.ping.navigation.EditScreen
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.const.FirestoreKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class EditType {
    Name,
    Bio,
}

data class EditScreenUiState(
    var label: String = "Sample label",
    var value: String = "Sample value",
    var isLoading: Boolean = false,
    var closeScreen: Boolean = false,
    var error: String = "",
)

@HiltViewModel
class EditScreenViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    currentUserInfo: CurrentUserInfo,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditScreenUiState())

    val uiState = _uiState.asStateFlow()

    private val me: User = currentUserInfo.currentUser

    private val type: EditType

    init {
        savedStateHandle.toRoute<com.joshgm3z.ping.navigation.EditScreen>().let { it ->
            type = it.type
            when (it.type) {
                EditType.Name -> _uiState.update {
                    it.copy(
                        label = "name",
                        value = me.name
                    )
                }

                EditType.Bio -> _uiState.update {
                    it.copy(
                        label = "bio",
                        value = me.about
                    )
                }
            }
        }
    }

    fun updateValue(newValue: String) {
        _uiState.update {
            it.copy(
                isLoading = true,
                error = "",
            )
        }
        val key = when (type) {
            EditType.Name -> FirestoreKey.User.name
            EditType.Bio -> FirestoreKey.User.about
        }
        viewModelScope.launch {
            userRepository.updateUserInfo(
                key = key,
                value = newValue,
                onUpdated = {
                    _uiState.update {
                        it.copy(
                            closeScreen = true,
                        )
                    }
                },
                onError = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Error updating info"
                        )
                    }
                },
            )
        }
    }
}