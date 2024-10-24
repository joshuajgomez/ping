package com.joshgm3z.repository.api

import com.joshgm3z.data.model.Chat
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun uploadChat(
        chat: Chat,
        onError: () -> Unit = {},
        onUploaded: () -> Unit,
    )

    fun observeChatsForUserLocal(userId: String): Flow<List<Chat>>

    fun observeChatsForUserHomeLocal(userId: String): Flow<List<Chat>>

    fun observerChatsForMeFromServer(): Job

    fun updateChatStatusToServer(newStatus: Long, chats: List<Chat>)

    suspend fun getChat(chatId: String): Chat
}