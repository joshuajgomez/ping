package com.joshgm3z.ping.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.utils.Logger

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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("select * from User where docId = :userId")
    suspend fun getUser(userId: String): User

    @Query("select * from User")
    suspend fun getAll(): List<User>

    @Transaction
    suspend fun insertAll(userList: List<User>) {
        for (user in userList) {
            insert(user)
        }
    }
}
