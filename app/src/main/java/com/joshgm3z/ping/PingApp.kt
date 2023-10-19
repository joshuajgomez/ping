package com.joshgm3z.ping

import android.app.Application
import com.joshgm3z.ping.model.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PingApp:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PingApp)
            modules(appModule)
        }
    }
}