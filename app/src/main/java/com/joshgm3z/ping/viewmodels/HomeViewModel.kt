package com.joshgm3z.ping.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.utils.randomUserList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(pingRepository: PingRepository) : ViewModel() {

    var userList: List<User> = listOf()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            pingRepository.refreshUserList()
            userList = pingRepository.getAllUsers()
        }
    }
}
