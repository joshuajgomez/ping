package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class IconPickerUiState {
    data object Fetching : IconPickerUiState()
    data object Empty : IconPickerUiState()
    data class Ready(val currentImageUrl: String, val imageUrls: List<String>) : IconPickerUiState()
    data class Error(val message: String) : IconPickerUiState()
}

@HiltViewModel
class IconPickerViewModel
@Inject constructor(
    private val currentUserInfo: CurrentUserInfo,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<IconPickerUiState>(IconPickerUiState.Fetching)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            imageRepository.fetchProfileIconUrls(
                onFetch = {
                    _uiState.value = when {
                        it.isNotEmpty() -> IconPickerUiState.Ready(
                            currentUserInfo.currentUser.imagePath,
                            it
                        )

                        else -> IconPickerUiState.Empty
                    }
                },
                onError = {
                    _uiState.value = IconPickerUiState.Error(it)
                }
            )
        }
    }
}
