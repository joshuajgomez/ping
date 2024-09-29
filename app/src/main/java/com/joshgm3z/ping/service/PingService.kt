package com.joshgm3z.ping.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.joshgm3z.ping.model.PingRepository
import com.joshgm3z.ping.utils.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PingService : Service() {

    @Inject
    lateinit var pingRepository: PingRepository

    companion object {
        var isRunning: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        Logger.entry()
        isRunning = true
        pingRepository.observerChatsForMeFromServer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, START_STICKY)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }
}