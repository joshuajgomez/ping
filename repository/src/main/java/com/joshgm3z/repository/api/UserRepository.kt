package com.joshgm3z.repository.api

import android.net.Uri
import com.joshgm3z.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): List<User>

    fun syncUserListWithServer(onUserListUpdated: () -> Unit)

    fun checkUserInServer(
        name: String,
        onCheckComplete: (user: User) -> Unit,
        onNotFound: () -> Unit,
        onCheckError: () -> Unit,
    )

    fun createUserInServer(
        user: User,
        registerComplete: (name: String) -> Unit,
        onError: (message: String) -> Unit,
    )

    suspend fun getUser(userId: String): User

    fun getUserFlow(userId: String): Flow<User>

    suspend fun signOutUser()

    suspend fun updateUserImageToServer(imageRes: Int)

    suspend fun uploadImage(
        url: String,
        onProgress: (progress: Float) -> Unit,
        onImageSaved: () -> Unit,
        onFailure: () -> Unit,
    )
}