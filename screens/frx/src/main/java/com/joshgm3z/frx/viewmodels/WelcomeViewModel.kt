package com.joshgm3z.frx.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.repository.api.ChatRepository
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
    private val chatRepository: ChatRepository,
    @MainThreadScope private val mainScope: CoroutineScope,
) : ViewModel() {

    fun downloadUserList(onDownloadComplete: () -> Unit) {
        Logger.entry()
        userRepository.syncUserListWithServer {
            chatRepository.observerChatsForMeFromServer()
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