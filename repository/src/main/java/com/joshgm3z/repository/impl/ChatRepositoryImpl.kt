package com.joshgm3z.repository.impl

import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.firebase.FirestoreDb
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.room.ChatDao
import com.joshgm3z.repository.room.PingDb
import com.joshgm3z.repository.room.UserDao
import com.joshgm3z.utils.Logger
import com.joshgm3z.utils.NotificationUtil
import com.joshgm3z.utils.const.FirestoreKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatRepositoryProvider {
    @Binds
    abstract fun provideChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

}

@Singleton
class ChatRepositoryImpl
@Inject
constructor(
    private val scope: CoroutineScope,
    private val db: PingDb,
    private val firestoreDb: FirestoreDb,
    private val currentUserInfo: CurrentUserInfo,
    private val notificationUtil: NotificationUtil,
) : ChatRepository {

    private val chatDao: ChatDao
        get() = db.chatDao()

    private val userDao: UserDao
        get() = db.userDao()

    init {
        firestoreDb.checkIfChatCollectionIsCleared {
            scope.launch {
                Logger.warn("chats_list cleared in server, clearing same in local db")
                chatDao.clearChats()
            }
        }
        scope.launch {
            observerChatsForMeFromServer()
        }
    }

    override fun createChatDocId(): String =
        firestoreDb.createChatDocId()

    override fun uploadChatWithId(
        chat: Chat,
        onError: () -> Unit,
        onUploaded: () -> Unit,
    ) {
        Logger.debug("chat = [${chat}]")
        chat.status = Chat.SENT
        firestoreDb.registerChatWithId(
            chat,
            onSuccess = {
                // chat added to firestore
                onUploaded()
            },
            onError = {
                // error adding chat
                Logger.warn("error adding chat")
                scope.launch {
                    chat.status = Chat.SAVED
                    chatDao.insert(chat)
                }
                onError()
            }
        )
        scope.launch {
            Thread.sleep(5000)
            addDummyChat(chat)
        }
    }

    /**
     * For testing purpose
     */
    private fun addDummyChat(chat: Chat) {
        val dummy = Chat(chat.message + " returned")
        dummy.toUserId = chat.fromUserId
        dummy.fromUserId = chat.toUserId
        dummy.sentTime = System.currentTimeMillis()
        dummy.status = Chat.SENT
        dummy.fileOnlineUrl = chat.fileOnlineUrl
        dummy.fileName = chat.fileName
        dummy.fileSize = chat.fileSize
        dummy.fileType = chat.fileType
        if (chat.message.contains("reply")) {
            dummy.replyToChatId = chat.docId
        }
        firestoreDb.registerChat(dummy, {}, {})
    }


    override fun observeChatsForUserLocal(userId: String): Flow<List<Chat>> {
        return chatDao.getChatsOfUserTimeDesc(userId)
    }

    override suspend fun getChatsForUser(userId: String): List<Chat> {
        return chatDao.getChatsOfUserTimeDesc(userId).first()
    }

    override fun observeChatsForUserHomeLocal(): Flow<List<Chat>> {
        return chatDao.getAllChatsTimeAsc()
    }

    override fun observeChatsForFileDownload(): Flow<List<Chat>> {
        return chatDao.getAllChatsForDownload()
    }

    override fun observerChatsForMeFromServer() = scope.launch {
        if (!currentUserInfo.isSignedIn) {
            Logger.error("user not signed in")
            return@launch
        }
        Logger.entry()

        val me = currentUserInfo.currentUser
        firestoreDb.listenForChatToOrFromUser(me.docId) {
            scope.launch {
                val firestoreList = mutableListOf<Chat>()
                it.forEach {
                    with(it) {
                        if (toUserId == me.docId) {
                            val user: User = userDao.getUser(fromUserId)
                            // necessary null check
                            fromUserName = user?.name ?: "Unknown"
                            if (status == Chat.SENT) {
                                status = Chat.DELIVERED
                                notificationUtil.showNotification(
                                    sentTime.toInt(),
                                    user.name,
                                    fromUserId,
                                    message
                                )
                                firestoreList.add(this)
                            }
                        } else {
                            isOutwards = true
                            fromUserName = "You"
                        }
                    }
                }
                firestoreDb.updateChatList(firestoreList, FirestoreKey.Chat.status, Chat.DELIVERED)
                chatDao.insertAll(it)
            }
        }
    }

    override fun updateChatStatusToServer(newStatus: Long, chats: List<Chat>) =
        chats.forEach { it ->
            with(it) {
                when {
                    newStatus == Chat.READ && status == Chat.DELIVERED -> Chat.READ
                    newStatus == Chat.DELIVERED && status == Chat.SENT -> Chat.DELIVERED
                    else -> null
                }?.let {
                    firestoreDb.updateChat(
                        this.docId,
                        FirestoreKey.Chat.status,
                        it
                    )
                }
            }
        }

    override suspend fun getChat(chatId: String): Chat =
        chatDao.getChat(chatId).first().first()

    override fun updateChat(chatId: String, key: String, value: String) {
        firestoreDb.updateChat(chatId, key, value)
    }

    override fun insertLocal(chat: Chat) {
        Logger.debug("chat = [${chat}]")
        scope.launch {
            chatDao.insert(chat)
        }
    }

    override fun updateChatLocal(chat: Chat) {
        scope.launch {
            chatDao.update(chat)
        }
    }

    override fun updateProgress(chatId: String, progress: Float) {
        scope.launch {
            chatDao.updateProgress(chatId, progress)
        }
    }

    override fun updateLocalFileUrl(chatId: String, fileUrl: String) {
        Logger.debug("chatId = [${chatId}], fileUrl = [${fileUrl}]")
        scope.launch {
            chatDao.updateLocalUrl(chatId, fileUrl)
        }
    }

    override suspend fun searchChat(query: String): List<Chat> {
        return chatDao.searchChats(query)
    }

    override suspend fun clearChats(
        userId: String,
        chats: List<Chat>,
        onComplete: () -> Unit,
        onError: (String) -> Unit,
    ) {
        firestoreDb.clearChatsFromOrToUser(
            chats = chats,
            onComplete = {
                scope.launch {
                    chatDao.clearChatsFromOrToUser(userId)
                    onComplete()
                }
            },
            onError = onError
        )
    }
}