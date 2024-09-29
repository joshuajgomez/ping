package com.joshgm3z.ping.model

import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.User
import com.joshgm3z.ping.model.firestore.FirestoreDb
import com.joshgm3z.ping.model.room.PingDb
import com.joshgm3z.ping.utils.Logger
import com.joshgm3z.ping.utils.DataStoreUtil
import com.joshgm3z.ping.utils.NotificationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PingRepository
@Inject constructor(
    private val scope: CoroutineScope,
    private val db: PingDb,
    private val firestoreDb: FirestoreDb,
    private val dataStore: DataStoreUtil,
    private val notificationUtil: NotificationUtil,
) {

    init {
        scope.launch {
            observerChatsForMeFromServer()
        }
    }

    suspend fun getUsers(): List<User> = db.userDao().getAll()

    fun syncUserListWithServer(onUserListUpdated: () -> Unit) {
        Logger.entry()
        firestoreDb.getUserList {
            Logger.debug("it = [$it]")
            if (it.isNotEmpty()) {
                runBlocking {
                    if (isUserSignedIn()) {
                        val currentUser = dataStore.getCurrentUser()
                        db.userDao().insertAll(it, currentUser.docId)
                        onUserListUpdated()
                    } else {
                        Logger.warn("current user is null")
                    }

                }
            }
        }
    }

    suspend fun uploadNewMessage(chat: Chat) {
        Logger.debug("chat = [${chat}]")
        chat.status = Chat.SENT
        firestoreDb.registerChat(chat,
            // chat added to firestore
            {
            },
            // error adding chat
            {
                Logger.warn("error adding chat")
                runBlocking {
                    chat.status = Chat.SAVED
                    db.chatDao().insert(chat)
                }
            }
        )
        runBlocking {
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
        firestoreDb.registerChat(dummy, {}, {})
    }

    fun checkUserInServer(name: String, onCheckComplete: (user: User?) -> Unit) {
        firestoreDb.checkUser(name) {
            runBlocking {
                if (it != null) {
                    dataStore.setUser(it)
                    observerChatsForMeFromServer()
                }
            }
            onCheckComplete(it)
        }
    }

    fun createUserInServer(
        name: String,
        imagePath: String,
        registerComplete: (isSuccess: Boolean, message: String) -> Unit,
    ) {
        val newUser = User(name)
        newUser.imagePath = imagePath
        firestoreDb.createUser(newUser) { user, message ->
            if (user != null) {
                runBlocking {
                    dataStore.setUser(user)
                    observerChatsForMeFromServer()
                    registerComplete(true, message)
                }
            } else {
                registerComplete(false, message)
            }
        }
    }

    suspend fun getUser(userId: String): User {
        return db.userDao().getUser(userId)
    }

    fun observeChatsForUserLocal(userId: String): Flow<List<Chat>> {
        return db.chatDao().getChatsOfUserTimeDesc(userId)
    }

    fun observeChatsForUserHomeLocal(userId: String): Flow<List<Chat>> {
        return db.chatDao().getChatsOfUserTimeAsc(userId)
    }

    fun observerChatsForMeFromServer() = scope.launch {
        if (!isUserSignedIn()) {
            Logger.error("user not signed in")
            return@launch
        }
        Logger.entry()

        val me = dataStore.getCurrentUser()
        firestoreDb.listenForChatToOrFromUser(me.docId) {
            runBlocking {
                it.forEach(action = {
                    if (it.toUserId == me.docId && it.status == Chat.SENT) {
                        it.status = Chat.DELIVERED
                        val user = db.userDao().getUser(it.fromUserId)
                        notificationUtil.showNotification(
                            it.sentTime.toInt(),
                            user,
                            it.fromUserId,
                            it.message
                        )
                        firestoreDb.updateChatStatus(it)
                    }
                    db.chatDao().insert(it)
                })
            }
        }
    }

    fun updateChatStatusToServer(status: Long, chats: List<Chat>) {
        chats.forEach(
            action = {
                if (status == Chat.READ && it.status == Chat.DELIVERED) {
                    it.status = Chat.READ
                    firestoreDb.updateChatStatus(it)
                } else if (status == Chat.DELIVERED && it.status == Chat.SENT) {
                    it.status = Chat.DELIVERED
                    firestoreDb.updateChatStatus(it)
                }
            }
        )
    }

    suspend fun getCurrentUser(): User = dataStore.getCurrentUser()

    fun isUserSignedIn(): Boolean = dataStore.isUserSignedIn()

    suspend fun signOutUser() {
        Logger.entry()
        dataStore.removeCurrentUser()
        firestoreDb.removeChatListener()
        db.chatDao().clearChats()
        db.userDao().clearUsers()
    }

    suspend fun updateUserImageToServer(imageRes: Int) {
        if (isUserSignedIn()) {
            val me = dataStore.getCurrentUser()
            me.imagePath = imageRes.toString()
            firestoreDb.updateUserImage(me) {
                runBlocking {
                    dataStore.setUser(it)
                }
            }
        } else {
            Logger.error("current user is null")
        }
    }

}
