package com.joshgm3z.ping.model

import androidx.lifecycle.LiveData
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.model.room.PingDb

class PingRepository(private val db:PingDb) {

    fun getChatForUser(userId: String): LiveData<List<Chat>> = db.chatDao().getChatForUser(userId)

    fun getAllUsers(): LiveData<List<User>> = db.userDao().getAll()

    suspend fun addChat(chat: Chat){
        db.chatDao().insert(chat)
    }
}