package com.joshgm3z.repository

import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.firebase.FirebaseStorage
import com.joshgm3z.firebase.FirestoreDb
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.repository.room.ChatDao
import com.joshgm3z.repository.room.PingDb
import com.joshgm3z.repository.room.UserDao
import com.joshgm3z.utils.Logger
import com.joshgm3z.utils.MainThreadScope
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
abstract class RepositoryProvider {
    @Binds
    abstract fun provideChatRepository(
        pingRepository: PingRepository
    ): ChatRepository

    @Binds
    abstract fun provideUserRepository(
        pingRepository: PingRepository
    ): UserRepository
}

@Singleton
class PingRepository
@Inject constructor(
    private val scope: CoroutineScope,
    @MainThreadScope private val mainScope: CoroutineScope,
    private val db: PingDb,
    private val firestoreDb: FirestoreDb,
    private val firebaseStorage: FirebaseStorage,
    private val currentUserInfo: CurrentUserInfo,
    private val notificationUtil: NotificationUtil,
) : ChatRepository, UserRepository {

    private val chatDao: ChatDao
        get() = db.chatDao()

    private val userDao: UserDao
        get() = db.userDao()

    init {
        scope.launch {
            observerChatsForMeFromServer()
        }
    }

    override suspend fun getUsers(): List<User> = userDao.getAll()

    override fun syncUserListWithServer(onUserListUpdated: () -> Unit) {
        Logger.entry()
        firestoreDb.getUserList {
            Logger.debug("it = [$it]")
            if (it.isNotEmpty()) {
                scope.launch {
                    if (currentUserInfo.isSignedIn) {
                        val currentUser = currentUserInfo.currentUser
                        userDao.insertAll(it, currentUser.docId)
                        observerChatsForMeFromServer()
                        mainScope.launch {
                            Logger.debug("user list updated")
                            onUserListUpdated()
                        }
                    } else {
                        Logger.warn("current user is null")
                    }

                }
            }
        }
    }

    override fun uploadNewMessage(
        chat: Chat,
        onMessageUploaded: () -> Unit,
    ) {
        Logger.debug("chat = [${chat}]")
        chat.status = Chat.SENT
        firestoreDb.registerChat(
            chat,
            onIdSet = {
                // chat added to firestore
                onMessageUploaded()
            },
            onError = {
                // error adding chat
                Logger.warn("error adding chat")
                scope.launch {
                    chat.status = Chat.SAVED
                    chatDao.insert(chat)
                }
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

    override fun checkUserInServer(
        name: String,
        onCheckComplete: (user: User) -> Unit,
        onNotFound: () -> Unit,
        onCheckError: () -> Unit,
    ) {
        Logger.info("name=$name")
        firestoreDb.checkUser(
            name,
            onCheckComplete = {
                scope.launch {
                    currentUserInfo.currentUser = it
                    mainScope.launch {
                        onCheckComplete(it)
                    }
                }
            },
            onNotFound = onNotFound,
            onCheckError = onCheckError,
        )

    }

    override fun createUserInServer(
        user: User,
        registerComplete: (name: String) -> Unit,
        onError: (message: String) -> Unit,
    ) {
        Logger.entry
        firestoreDb.createUser(
            user,
            onUserCreated = {
                scope.launch {
                    currentUserInfo.currentUser = it
                    registerComplete(it.name)
                }
            },
            onError = onError
        )

    }

    override suspend fun getUser(userId: String): User =
        userDao.getUser(userId)

    override fun getUserFlow(userId: String): Flow<User> =
        userDao.getUserFlow(userId)

    override fun observeChatsForUserLocal(userId: String): Flow<List<Chat>> {
        return chatDao.getChatsOfUserTimeDesc(userId)
    }

    override fun observeChatsForUserHomeLocal(userId: String): Flow<List<Chat>> {
        return chatDao.getChatsOfUserTimeAsc(userId)
    }

    private fun observerChatsForMeFromServer() = scope.launch {
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
                        val user: User = userDao.getUser(fromUserId) ?: return@with
                        if (toUserId == me.docId) {
                            if (status == Chat.SENT) {
                                status = Chat.DELIVERED
                                notificationUtil.showNotification(
                                    sentTime.toInt(),
                                    user.name,
                                    fromUserId,
                                    message
                                )
                                firestoreDb.updateChatStatus(this)
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
                    status = it
                    firestoreDb.updateChatStatus(this)
                }
            }
        }

    override suspend fun getChat(chatId: String): Chat =
        chatDao.getChat(chatId).first().first()

    override suspend fun signOutUser() {
        Logger.entry()
        currentUserInfo.removeCurrentUser()
        firestoreDb.removeChatListener()
        db.clearAllTables()
    }

    override fun uploadChatImage(
        chat: Chat,
        imageUrl: String,
        onImageSent: () -> Unit,
        onProgress: (Int) -> Unit,
        onError: (String) -> Unit,
    ) {
        Logger.debug("chat = [${chat}], imageUrl = [${imageUrl}]")
        firebaseStorage.uploadImage(
            fileName = "chat_${System.currentTimeMillis()}.jpg",
            url = imageUrl,
            onUploadComplete = {
                chat.imageUrl = it
                uploadNewMessage(chat) {
                    onImageSent()
                }
            },
            onUploadProgress = {
                Logger.debug("$it%")
            },
            onUploadFailed = onError
        )
    }

    override suspend fun uploadImage(
        url: String,
        onProgress: (progress: Float) -> Unit,
        onImageSaved: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        Logger.debug("uri = [${url}]")
        firebaseStorage.uploadImage(
            fileName = "profile_${currentUserInfo.currentUser.docId}.jpg",
            url = url,
            onUploadComplete = {
                scope.launch {
                    updateUserInfo(
                        key = FirestoreKey.User.imagePath,
                        value = it,
                        onUpdated = onImageSaved,
                        onError = onFailure
                    )
                }
            },
            onUploadProgress = {
                Logger.debug("$it%")
                onProgress(it)
            },
            onUploadFailed = onFailure
        )
    }

    override suspend fun updateUserInfo(
        key: String,
        value: String,
        onUpdated: () -> Unit,
        onError: (String) -> Unit,
    ) {
        Logger.debug("key = [${key}], value = [${value}]")
        firestoreDb.updateUserInfo(
            userId = currentUserInfo.currentUser.docId,
            key = key,
            value = value,
            onUpdateComplete = {
                currentUserInfo.currentUser = it
                mainScope.launch {
                    onUpdated()
                }
            },
            onUpdateError = onError
        )
    }

}
