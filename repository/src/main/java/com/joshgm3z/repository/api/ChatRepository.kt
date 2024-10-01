package com.joshgm3z.repository.api

import com.joshgm3z.data.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun uploadNewMessage(chat: Chat)

    fun observeChatsForUserLocal(userId: String): Flow<List<Chat>>

    fun observeChatsForUserHomeLocal(userId: String): Flow<List<Chat>>

    fun updateChatStatusToServer(status: Long, chats: List<Chat>)
}