package com.joshgm3z.ping.model.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.joshgm3z.ping.utils.getRandomFromTo
import com.joshgm3z.ping.utils.randomChat
import com.joshgm3z.ping.utils.randomMessage
import com.joshgm3z.ping.utils.randomName
import com.joshgm3z.ping.utils.randomUser
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
    var status: Long = SAVED

    @Ignore
    var isOutwards: Boolean = true

    companion object {
        fun random(): Chat {
            val chat = Chat(randomMessage())
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
                " isOutwards=$isOutwards, status=${textStatus(status)})"
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

@Entity
data class User(var name: String = "") {
    @PrimaryKey
    var docId: String = ""
    var imagePath: String = ""

    companion object {
        fun random(): User {
            val user = User(randomName())
            user.docId = System.currentTimeMillis().toString()
            user.imagePath = ""
            return user
        }
    }

    override fun toString(): String {
        return "User(name='$name', docId='$docId', picture=$imagePath)"
    }
}

class HomeChat {
    var otherGuy: User = User("Someone")
    var lastChat: Chat = Chat("hey")
    var count: Int = Random.nextInt(15)

    companion object {
        fun random() = HomeChat()
    }

    override fun toString(): String {
        return "HomeChat(otherGuy=$otherGuy, lastChat=$lastChat, count=$count)"
    }

}