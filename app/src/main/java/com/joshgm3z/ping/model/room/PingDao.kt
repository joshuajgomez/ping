package com.joshgm3z.ping.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User

@Dao
interface ChatDao {
    @Insert
    suspend fun insert(chat: Chat)

    @Query("select * from Chat where fromUserId = :userId or toUserId = :userId")
    fun getChatForUser(userId: String): LiveData<List<Chat>>

    @Update
    suspend fun update(chat: Chat)
}

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("select * from User")
    fun getAll(): LiveData<List<User>>
}
