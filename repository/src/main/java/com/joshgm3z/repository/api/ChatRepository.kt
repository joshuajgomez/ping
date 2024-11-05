package com.joshgm3z.repository.api

import com.joshgm3z.data.model.Chat
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun uploadChatWithId(
        chat: Chat,
        onError: () -> Unit = {},
        onUploaded: () -> Unit,
    )

    fun observeChatsForUserLocal(userId: String): Flow<List<Chat>>

    suspend fun getChatsForUser(userId: String): List<Chat>

    fun createChatDocId(): String

    fun observeChatsForUserHomeLocal(): Flow<List<Chat>>

    fun observeChatsForFileDownload(): Flow<List<Chat>>

    fun observerChatsForMeFromServer(): Job

    fun updateChatStatusToServer(newStatus: Long, chats: List<Chat>)

    suspend fun getChat(chatId: String): Chat

    fun updateChat(chatId: String, key: String, value: String)

    fun insertLocal(chat: Chat)

    fun updateChatLocal(chat: Chat)

    fun updateProgress(chatId: String, progress: Float)

    fun updateLocalFileUrl(chatId: String, fileUrl: String)

    suspend fun searchChat(query: String): List<Chat>

    suspend fun clearChats(
        userId: String,
        chats: List<Chat>,
        onComplete: () -> Unit,
        onError: (String) -> Unit,
    )
}