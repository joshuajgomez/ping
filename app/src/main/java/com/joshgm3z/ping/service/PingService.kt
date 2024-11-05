package com.joshgm3z.ping.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.joshgm3z.repository.download.DownloadManager
import com.joshgm3z.repository.api.ChatRepository
import com.joshgm3z.utils.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PingService : Service() {

    @Inject
    lateinit var chatRepository: ChatRepository

    @Inject
    lateinit var downloadManager: DownloadManager

    companion object {
        fun start(context: Context) {
            Logger.debug("PingService.isRunning = [$isRunning]")
            Intent(context, PingService::class.java).apply {
                context.startService(this)
            }
        }

        var isRunning: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        Logger.entry()
        isRunning = true
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