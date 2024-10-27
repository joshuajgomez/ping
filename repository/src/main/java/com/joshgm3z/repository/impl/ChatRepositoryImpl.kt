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
        scope.launch {
            observerChatsForMeFromServer()
        }
    }

    override fun createChatDocId(): String =
        firestoreDb.createChatDocId()

    override fun uploadChat(
        chat: Chat,
        onError: () -> Unit,
        onUploaded: (String) -> Unit,
    ) {
        Logger.debug("chat = [${chat}]")
        chat.status = Chat.SENT
        firestoreDb.registerChat(
            chat,
            onIdSet = {
                // chat added to firestore
                onUploaded(it)
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
        dummy.imageUrl = chat.imageUrl
        firestoreDb.registerChat(dummy, {}, {})
    }


    override fun observeChatsForUserLocal(userId: String): Flow<List<Chat>> {
        return chatDao.getChatsOfUserTimeDesc(userId)
    }

    override fun observeChatsForUserHomeLocal(userId: String): Flow<List<Chat>> {
        return chatDao.getChatsOfUserTimeAsc(userId)
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
                it.forEach {
                    with(it) {
                        val user: User = userDao.getUser(fromUserId)
                        if (toUserId == me.docId) {
                            if (status == Chat.SENT) {
                                status = Chat.DELIVERED
                                notificationUtil.showNotification(
                                    sentTime.toInt(),
                                    user.name,
                                    fromUserId,
                                    message
                                )
                                firestoreDb.updateChat(
                                    this.docId,
                                    FirestoreKey.Chat.status,
                                    status
                                )
                            }
                        } else {
                            isOutwards = true
                        }
                        chatDao.insert(this)
                    }
                }
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

    override suspend fun updateChat(chatId: String, key: String, value: String) {
        firestoreDb.updateChat(chatId, key, value)
    }

    override suspend fun insertLocal(chat: Chat) {
        Logger.debug("chat = [${chat}]")
        chatDao.insert(chat)
    }

    override suspend fun updateChatLocal(chat: Chat) {
        chatDao.update(chat)
    }

    override suspend fun searchChat(query: String): List<Chat> {
        return chatDao.searchChats(query)
    }
}