package com.joshgm3z.repository.api

import com.joshgm3z.data.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun uploadNewMessage(
        chat: Chat,
        onMessageUploaded: () -> Unit
    )

    fun uploadImage(
        chat: Chat,
        imageUrl: String,
        onImageSent: () -> Unit,
        onProgress: (Int) -> Unit,
        onError: () -> Unit,
    )

    fun observeChatsForUserLocal(userId: String): Flow<List<Chat>>

    fun observeChatsForUserHomeLocal(userId: String): Flow<List<Chat>>

    fun updateChatStatusToServer(newStatus: Long, chats: List<Chat>)

    suspend fun getChat(chatId: String): Chat
}