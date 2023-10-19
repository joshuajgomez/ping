package com.joshgm3z.ping.model.koin

import androidx.room.Room
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.chat.ChatViewModel
import com.joshgm3z.ping.model.room.PingDb
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            get(),
            PingDb::class.java, "ping-db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        PingRepository(get())
    }
    viewModel {
        ChatViewModel(get())
    }
}