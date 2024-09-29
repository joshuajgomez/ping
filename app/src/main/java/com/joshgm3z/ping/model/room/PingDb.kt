package com.joshgm3z.ping.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbProvider {
    @Provides
    fun provideDb(context: Context): PingDb =
        Room.databaseBuilder(
            context,
            PingDb::class.java, "ping-db"
        )
            .fallbackToDestructiveMigration()
            .build()
}

@Database(entities = [Chat::class, User::class], version = 8, exportSchema = false)
abstract class PingDb : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
}