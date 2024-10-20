package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.data.model.User
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import com.joshgm3z.utils.const.FirestoreKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IconPickerViewModel
@Inject constructor(
    private val currentUserInfo: CurrentUserInfo,
    private val userRepository: UserRepository,
) : ViewModel() {

    val me: User
        get() = currentUserInfo.currentUser

    fun onImageConfirmed(
        imageRes: Int,
        onImageSaved: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            userRepository.updateUserInfo(
                FirestoreKey.User.profileIcon,
                imageRes.toString(),
                onUpdated = onImageSaved,
                onError = onFailure,
            )
        }
    }

    fun saveImage(
        imageUrl: String,
        onImageSaved: () -> Unit,
        onProgress: (progress: Float) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        Logger.debug("imageUrl = [${imageUrl}]")
        viewModelScope.launch {
            userRepository.uploadImage(
                imageUrl,
                onProgress,
                onImageSaved,
                onFailure
            )
        }
    }
}
