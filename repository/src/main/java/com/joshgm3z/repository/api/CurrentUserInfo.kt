package com.joshgm3z.repository.api

import com.joshgm3z.data.model.User
import com.joshgm3z.repository.DataStoreUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentUserInfo
@Inject constructor(
    private val dataStore: DataStoreUtil
) {
    val isSignedIn: Boolean
        get() = dataStore.isUserSignedIn()

    var currentUser: User
        get() = dataStore.getCurrentUser()
        set(value) {
            dataStore.setUser(value)
        }

    fun removeCurrentUser() =
        dataStore.removeCurrentUser()
}