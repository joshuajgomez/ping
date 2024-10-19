package com.joshgm3z.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.joshgm3z.data.util.randomMessage
import kotlin.random.Random

@Entity
data class Chat(
    val message: String,
) {
    @PrimaryKey
    var docId: String = ""
    var sentTime: Long = 0
    var fromUserId: String = ""
    var toUserId: String = ""
    var replyToChatId: String = ""
    var status: Long = SAVED
    var imageUrl: String = ""
    var isOutwards: Boolean = false

    companion object {
        fun random(message: String = randomMessage()): Chat {
            val chat = Chat(message)
            chat.sentTime = System.currentTimeMillis()
            chat.fromUserId = ""
            chat.toUserId = ""
            chat.isOutwards = Random.nextBoolean()
            return chat
        }

        const val SAVED: Long = 0
        const val SENT: Long = 1
        const val DELIVERED: Long = 2
        const val READ: Long = 3
    }

    override fun toString(): String {
        return "Chat(message='$message', docId='$docId'," +
                " sentTime=$sentTime, fromUserId=$fromUserId, toUserId=$toUserId," +
                " isOutwards=$isOutwards, status=${textStatus(status)}," +
                " imageUrl=${imageUrl})"
    }

    private fun textStatus(status: Long): String {
        return when (status) {
            SENT -> "SENT"
            DELIVERED -> "DELIVERED"
            READ -> "READ"
            else -> "SAVED"
        }
    }


}