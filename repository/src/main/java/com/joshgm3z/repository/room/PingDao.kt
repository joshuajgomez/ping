package com.joshgm3z.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chat: Chat)

    @Query("select * from Chat where fromUserId = :userId or toUserId = :userId order by sentTime desc")
    fun getChatsOfUserTimeDesc(userId: String): Flow<List<Chat>>

    @Query("select * from Chat where fromUserId = :userId or toUserId = :userId order by sentTime asc")
    fun getChatsOfUserTimeAsc(userId: String): Flow<List<Chat>>

    @Update
    suspend fun update(chat: Chat)

    @Transaction
    suspend fun insertAll(chats: List<Chat>) {
        for (chat in chats) {
            insert(chat)
        }
    }

    @Query("delete from Chat")
    fun clearChats()

    @Query("select * from Chat where docId = :chatId")
    fun getChat(chatId: String): Flow<List<Chat>>
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
    suspend fun insertAll(userList: List<User>, exceptId: String) {
        for (user in userList) {
            if (user.docId != exceptId)
                insert(user)
        }
    }

    @Query("delete from User")
    fun clearUsers()
}
