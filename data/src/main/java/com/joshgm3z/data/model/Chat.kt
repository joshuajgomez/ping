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
    var fromUserName: String = ""
    var toUserId: String = ""
    var replyToChatId: String = ""
    var status: Long = SAVED
    var imageUrl: String = ""
    var isOutwards: Boolean = false
    var imageUploadUri: String = ""
    var imageUploadProgress: Float = 0f

    @get:Ignore
    val webUrl: String
        get() = with(message) {
            when {
                this == null -> ""
                contains("http") -> {
                    val start = indexOf("http")
                    val end = indexOf(" ", startIndex = start)
                    return substring(start, if (end == -1) length else end)
                }

                else -> ""
            }
        }

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
                " isOutwards=$isOutwards," +
                " replyToChatId=$replyToChatId," +
                " status=${textStatus(status)}," +
                " imageUrl=${imageUrl}," +
                " imageUpload=[${imageUploadProgress}]>>${imageUploadUri}," +
                ")"
    }

    private fun textStatus(status: Long): String {
        return when (status) {
            SENT -> "SENT"
            DELIVERED -> "DELIVERED"
            READ -> "READ"
            else -> "SAVED"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chat

        if (message != other.message) return false
        if (docId != other.docId) return false
        if (sentTime != other.sentTime) return false
        if (fromUserId != other.fromUserId) return false
        if (toUserId != other.toUserId) return false
        if (replyToChatId != other.replyToChatId) return false
        if (status != other.status) return false
        if (imageUrl != other.imageUrl) return false
        if (isOutwards != other.isOutwards) return false
        if (imageUploadUri != other.imageUploadUri) return false
        if (imageUploadProgress != other.imageUploadProgress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + docId.hashCode()
        result = 31 * result + sentTime.hashCode()
        result = 31 * result + fromUserId.hashCode()
        result = 31 * result + toUserId.hashCode()
        result = 31 * result + replyToChatId.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + isOutwards.hashCode()
        result = 31 * result + imageUploadUri.hashCode()
        result = 31 * result + imageUploadProgress.hashCode()
        return result
    }


}