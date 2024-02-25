package com.joshgm3z.ping.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.User

@Database(entities = [Chat::class, User::class], version = 7, exportSchema = false)
abstract class PingDb:RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
}