package com.joshgm3z.repository.api

import com.joshgm3z.data.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>

    fun syncUserListWithServer(onUserListUpdated: () -> Unit)

    fun checkUserInServer(name: String, onCheckComplete: (user: User?) -> Unit)

    fun createUserInServer(
        name: String,
        imagePath: String,
        registerComplete: (isSuccess: Boolean, message: String) -> Unit,
    )

    suspend fun getUser(userId: String): User

    suspend fun signOutUser()

    suspend fun updateUserImageToServer(imageRes: Int)
}