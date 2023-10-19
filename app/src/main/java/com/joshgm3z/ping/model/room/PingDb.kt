package com.joshgm3z.ping.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User

@Database(entities = [Chat::class, User::class], version = 0, exportSchema = false)
abstract class PingDb:RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
}