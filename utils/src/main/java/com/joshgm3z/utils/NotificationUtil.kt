package com.joshgm3z.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.joshgm3z.utils.const.OPEN_CHAT_USER
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationUtil
@Inject
constructor(
    private val context: Context
) {
    var intent: Intent? = null

    private val channelDescription = "Ping will notify you when you receive new messages"
    private val channelName = "New messages"
    private val channelId = "new_messages"

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    fun showNotification(id: Int, fromUser: String, fromUserId: String, message: String) {
        Logger.debug(
            "id = [${id}], fromUser = [${fromUser}], " +
                    "fromUserId = [${fromUserId}], message = [${message}]"
        )
        intent?.putExtra(OPEN_CHAT_USER, fromUserId)
        Logger.debug("intent.extras = [${intent?.extras}]")
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_ping_foreground)
            .setContentTitle(fromUser)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(id, builder.build())
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        val name = channelName
        val descriptionText = channelDescription
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system.
        notificationManager.createNotificationChannel(channel)
    }

}