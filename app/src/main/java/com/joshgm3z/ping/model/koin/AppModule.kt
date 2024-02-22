package com.joshgm3z.ping.model.koin

import androidx.room.Room
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.ui.chat.ChatViewModel
import com.joshgm3z.ping.model.firestore.FirestoreDb
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
        FirestoreDb()
    }
    single {
        PingRepository(get(), get())
    }
    viewModel {
        ChatViewModel(get())
    }
}