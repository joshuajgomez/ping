package com.joshgm3z.ping.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.User
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.ImageRepository
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import com.joshgm3z.utils.const.FirestoreKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ImagePickerUiState(
    val imageUrl: String,
    val loading: Boolean = false,
    val loadingProgress: Float = 0f,
    val error: String = "",
    val notify: String = "",
)

const val keySelectedProfileIcon = "KEY_SELECTED_PROFILE_ICON"

@HiltViewModel
class ImagePickerViewModel
@Inject constructor(
    private val currentUserInfo: CurrentUserInfo,
    private val imageRepository: ImageRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val me: User
        get() = currentUserInfo.currentUser

    private val _uiState = MutableStateFlow(ImagePickerUiState(me.imagePath))
    val uiState = _uiState.asStateFlow()

    fun setSelectedImage(imageUrl: String) {
        Logger.debug("imageUrl = [${imageUrl}]")
        uploadImageUrl(imageUrl)
    }

    fun saveImage(
        imageUri: Uri,
    ) {
        Logger.debug("imageUrl = [${imageUri}]")
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            imageRepository.uploadImage(
                folderName = FirestoreKey.keyProfilePicture,
                fileName = "profile_${me.docId}.jpg",
                localUri = imageUri,
                onProgress = { _uiState.update { it.copy(loadingProgress = it.loadingProgress) } },
                onSuccess = {
                    when {
                        me.imagePath != it -> uploadImageUrl(it)
                        else -> onSuccess()
                    }
                },
                onError = { onError(it) }
            )
        }
    }

    private fun uploadImageUrl(url: String) = viewModelScope.launch {
        Logger.debug("uploadImageUrl: $url")
        _uiState.update {
            it.copy(loading = true)
        }
        userRepository.updateUserInfo(
            key = FirestoreKey.User.imagePath,
            value = url,
            onUpdated = { onSuccess() },
            onError = { onError(it) }
        )
    }

    fun removeImage() {
        Logger.entry()
        uploadImageUrl("")
    }

    private fun onError(error: String) = _uiState.update {
        Logger.debug("onError: $error")
        it.copy(
            loading = false,
            error = error
        )
    }

    private fun onSuccess() = _uiState.update {
        Logger.entry
        it.copy(
            loading = false,
            imageUrl = me.imagePath,
            notify = "Profile picture updated"
        )
    }.apply {
        viewModelScope.launch {
            delay(3000)
            _uiState.update {
                Logger.debug("removing notify")
                it.copy(notify = "")
            }
        }
    }

}
