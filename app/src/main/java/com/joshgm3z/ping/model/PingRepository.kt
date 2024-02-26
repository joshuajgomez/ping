package com.joshgm3z.ping.model

import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.User
import com.joshgm3z.ping.model.firestore.FirestoreDb
import com.joshgm3z.ping.model.room.PingDb
import com.joshgm3z.ping.utils.Logger
import com.joshgm3z.ping.utils.DataStoreUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PingRepository(
    private val db: PingDb,
    private val firestoreDb: FirestoreDb,
    private val dataStore: DataStoreUtil,
) {

    init {
        GlobalScope.launch {
            startFetchingChats()
        }
    }

    suspend fun getUsers(): List<User> = db.userDao().getAll()

    fun refreshUserList(onUserListUpdated: () -> Unit) {
        firestoreDb.getUserList {
            Logger.debug("it = [$it]")
            if (it.isNotEmpty()) {
                runBlocking {
                    db.userDao().insertAll(it, dataStore.getCurrentUser().docId)
                    onUserListUpdated()
                }
            }
        }
    }

    suspend fun addChat(chat: Chat) {
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

    private fun addDummyChat(chat: Chat) {
        Logger.debug("chat = [${chat}]")
        val dummy = Chat(chat.message + " returned")
        dummy.toUserId = chat.fromUserId
        dummy.fromUserId = chat.toUserId
        dummy.sentTime = System.currentTimeMillis()
        dummy.status = Chat.SENT
        firestoreDb.registerChat(dummy, {}, {})
    }

    fun checkUser(name: String, onCheckComplete: (user: User?) -> Unit) {
        firestoreDb.checkUser(name) { onCheckComplete(it) }
    }

    fun registerUser(
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
                    registerComplete(true, message)
                }
            } else {
                registerComplete(false, message)
            }
        }
    }

    suspend fun getUser(userId: String): User {
        val user = db.userDao().getUser(userId)
        Logger.debug("userId = [${userId}], user = [$user]")
        return user
    }

    fun getChatsOfUserForChatScreen(userId: String): Flow<List<Chat>> {
        return db.chatDao().getChatsOfUserTimeDesc(userId)
    }
    fun getChatsOfUserForHome(userId: String): Flow<List<Chat>> {
        return db.chatDao().getChatsOfUserTimeAsc(userId)
    }

    suspend fun startFetchingChats() {
        Logger.entry()
        val me: User = dataStore.getCurrentUser()
        firestoreDb.listenForChatToOrFromUser(me.docId) {
            runBlocking {
                it.forEach(action = {
                    Logger.debug("it = [$it]")
                    if (it.toUserId == me.docId && it.status == Chat.SENT) {
                        it.status = Chat.DELIVERED
                        firestoreDb.updateChatStatus(it)
                    }
                    db.chatDao().insert(it)
                })
            }
        }
    }

    fun updateChatStatus(status: Long, chats: List<Chat>) {
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
}
