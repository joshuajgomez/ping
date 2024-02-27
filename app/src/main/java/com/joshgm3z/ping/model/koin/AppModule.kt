package com.joshgm3z.ping.model.koin

import androidx.room.Room
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel
import com.joshgm3z.ping.model.firestore.FirestoreDb
import com.joshgm3z.ping.model.room.PingDb
import com.joshgm3z.ping.utils.DataStoreUtil
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.viewmodels.SearchViewModel
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.ping.utils.FirebaseLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            get(),
            PingDb::class.java, "ping-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        FirebaseLogger(get())
    }
    single {
        FirestoreDb(get())
    }
    single {
        DataStoreUtil(get())
    }
    single {
        PingRepository(get(), get(), get())
    }
    viewModel {
        ChatViewModel(get(), get())
    }
    viewModel {
        UserViewModel(get())
    }
    viewModel {
        HomeViewModel(get(), get())
    }
    viewModel {
        SearchViewModel(get())
    }
}