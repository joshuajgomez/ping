package com.joshgm3z.repository.api

import com.joshgm3z.data.model.Chat
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun uploadChat(
        chat: Chat,
        onError: () -> Unit = {},
        onUploaded: (String) -> Unit,
    )

    fun uploadChatWithId(
        chat: Chat,
        onError: () -> Unit = {},
        onUploaded: () -> Unit,
    )

    fun observeChatsForUserLocal(userId: String): Flow<List<Chat>>

    fun createChatDocId(): String

    fun observeChatsForUserHomeLocal(): Flow<List<Chat>>

    fun observerChatsForMeFromServer(): Job

    fun updateChatStatusToServer(newStatus: Long, chats: List<Chat>)

    suspend fun getChat(chatId: String): Chat

    suspend fun updateChat(chatId: String, key: String, value: String)

    suspend fun insertLocal(chat: Chat)

    suspend fun updateChatLocal(chat: Chat)

    suspend fun searchChat(query: String): List<Chat>
}