package com.joshgm3z.ping.model.koin

import androidx.room.Room
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.ui.viewmodels.ChatViewModel
import com.joshgm3z.ping.model.firestore.FirestoreDb
import com.joshgm3z.ping.model.room.PingDb
import com.joshgm3z.ping.utils.DataStoreUtil
import com.joshgm3z.ping.ui.viewmodels.HomeViewModel
import com.joshgm3z.ping.ui.viewmodels.SignInViewModel
import com.joshgm3z.ping.ui.viewmodels.UserViewModel
import com.joshgm3z.ping.utils.FirebaseLogger
import com.joshgm3z.ping.utils.NotificationUtil
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
        NotificationUtil(get())
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
        PingRepository(get(), get(), get(), get())
    }
    viewModel {
        ChatViewModel(get())
    }
    viewModel {
        HomeViewModel(get())
    }
    viewModel {
        UserViewModel(get())
    }
    viewModel {
        SignInViewModel(get())
    }
}