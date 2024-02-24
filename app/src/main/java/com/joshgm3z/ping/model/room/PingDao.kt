package com.joshgm3z.ping.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chat: Chat)

    @Query("select * from Chat where fromUserId = :userId or toUserId = :userId order by sentTime desc")
    fun getChatsOfUser(userId: String): Flow<List<Chat>>

    @Update
    suspend fun update(chat: Chat)

    @Transaction
    suspend fun insertAll(chats: List<Chat>) {
        for (chat in chats) {
            insert(chat)
        }
    }
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
