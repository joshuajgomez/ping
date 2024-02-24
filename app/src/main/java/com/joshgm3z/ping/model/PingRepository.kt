package com.joshgm3z.ping.model

import android.util.Log
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
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

    fun refreshUserList() {
        firestoreDb.getUserList {
            Logger.debug("it = [$it]")
            if (it.isNotEmpty()) {
                runBlocking {
                    db.userDao().insertAll(it)
                }
            }
        }
    }

    suspend fun addChat(chat: Chat) {
        db.chatDao().insert(chat)
        firestoreDb.registerChat(chat,
            // chat added to firestore
            {
                chat.docId = it
                runBlocking {
                    db.chatDao().update(chat)
                }
            },
            // error adding chat
            {
                Logger.warn("error adding chat")
            }
        )

        runBlocking {
            Thread.sleep(2000)
            addDummyChat(chat)
        }
    }

    private fun addDummyChat(chat: Chat) {
        Logger.debug("chat = [${chat}]")
        // dummy
        val dummy = Chat(chat.message + " returned")
        dummy.toUserId = chat.fromUserId
        dummy.fromUserId = chat.toUserId
        dummy.sentTime = System.currentTimeMillis()
        firestoreDb.registerChat(dummy,
            // chat added to firestore
            {
            },
            // error adding chat
            {
            }
        )
    }

    fun checkUser(name: String, onCheckComplete: (user: User?) -> Unit) {
        firestoreDb.checkUser(name) { onCheckComplete(it) }
    }

    fun registerUser(
        name: String,
        registerComplete: (isSuccess: Boolean, message: String) -> Unit,
    ) {
        val newUser = User(name)
        newUser.imagePath = ""
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

    fun getChatsOfUser(userId: String): Flow<List<Chat>> {
        return db.chatDao().getChatsOfUser(userId)
    }

    suspend fun startFetchingChats() {
        Logger.entry()
        val user: User = dataStore.getCurrentUser()
        firestoreDb.listenForChatToUser(user.docId) {
            runBlocking {
                db.chatDao().insertAll(it)
            }
        }
    }
}