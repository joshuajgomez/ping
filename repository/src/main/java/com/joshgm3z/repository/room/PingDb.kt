package com.joshgm3z.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DbProvider {
    @Provides
    fun provideDb(context: Context): PingDb =
        Room.databaseBuilder(context, PingDb::class.java, "ping-db")
            .fallbackToDestructiveMigration()
            .build()
}

@Database(
    entities = [Chat::class, User::class],
    version = 16,
    exportSchema = false
)
abstract class PingDb : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
}