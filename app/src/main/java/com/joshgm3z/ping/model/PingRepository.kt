package com.joshgm3z.ping.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.model.firestore.FirestoreDb
import com.joshgm3z.ping.model.room.PingDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PingRepository(
    private val db: PingDb,
    private val firestoreDb: FirestoreDb,
) {

    private val TAG = "PingRepository"

    fun getChatForUser(userId: String): LiveData<List<Chat>> = db.chatDao().getChatForUser(userId)

    fun getAllUsers(): LiveData<List<User>> = db.userDao().getAll()

    suspend fun addChat(chat: Chat) {
        db.chatDao().insert(chat)
        firestoreDb.registerChat(chat,
            // chat added to firestore
            {
                chat.docId = it
                GlobalScope.launch(Dispatchers.IO) {
                    db.chatDao().update(chat)
                }
            },
            // error adding chat
            {
                Log.w(TAG, "error adding chat")
            })
    }
}