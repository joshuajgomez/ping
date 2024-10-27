package com.joshgm3z.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.joshgm3z.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("select * from User where docId = :userId")
    suspend fun getUser(userId: String): User

    @Query("select * from User where docId = :userId")
    fun getUserFlow(userId: String): Flow<User>

    @Query("select * from User")
    suspend fun getAll(): List<User>

    @Transaction
    suspend fun insertAll(
        userList: List<User>,
        exceptId: String
    ) = userList.filter {
        it.docId != exceptId
    }.forEach {
        insert(it)
    }

    @Query("delete from User")
    fun clearUsers()

    @Query("select * from User where name like '%' || :query || '%'")
    fun searchUsers(query: String): List<User>
}