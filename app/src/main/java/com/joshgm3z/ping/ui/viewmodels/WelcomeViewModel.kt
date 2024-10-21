package com.joshgm3z.ping.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.utils.Logger
import com.joshgm3z.utils.MainThreadScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel
@Inject
constructor(
    private val userRepository: UserRepository,
    @MainThreadScope private val mainScope: CoroutineScope,
) : ViewModel() {

    fun downloadUserList(onDownloadComplete: () -> Unit) {
        Logger.entry()
        userRepository.syncUserListWithServer {
            viewModelScope.launch {
                delay(3000)
                Logger.debug("closing welcome screen")
                mainScope.launch {
                    onDownloadComplete()
                }
            }
        }
    }

}