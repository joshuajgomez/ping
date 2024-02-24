package com.joshgm3z.ping.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.model.firestore.FirestoreDb
import com.joshgm3z.ping.model.room.PingDb
import com.joshgm3z.ping.utils.Logger
import com.joshgm3z.ping.utils.DataStoreUtil
import kotlinx.coroutines.runBlocking

class PingRepository(
    private val db: PingDb,
    private val firestoreDb: FirestoreDb,
    private val dataStore: DataStoreUtil,
) {

    private val TAG = "PingRepository"

    fun getChatForUser(userId: String): LiveData<List<Chat>> = db.chatDao().getChatForUser(userId)

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
                Log.w(TAG, "error adding chat")
            })
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
}