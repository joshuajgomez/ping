package com.joshgm3z.ping.data

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
    @PrimaryKey(autoGenerate = true)
    var localId: Long = 0
    var docId: String = ""
    var sentTime: Long = 0
    var fromUserId: String = ""
    var toUserId: String = ""

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
    }

    override fun toString(): String {
        return "Chat(message='$message', localId=$localId, docId='$docId'," +
                " sentTime=$sentTime, fromUserId=$fromUserId, toUserId=$toUserId," +
                " isOutwards=$isOutwards)"
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
    var count: Int = 1

    companion object {
        fun random() = HomeChat()
    }

    override fun toString(): String {
        return "HomeChat(otherGuy=$otherGuy, lastChat=$lastChat, count=$count)"
    }

}