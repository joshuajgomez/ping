package com.joshgm3z.ping.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.joshgm3z.ping.R
import com.joshgm3z.ping.utils.getRandomFromTo
import com.joshgm3z.ping.utils.randomChat
import com.joshgm3z.ping.utils.randomName
import com.joshgm3z.ping.utils.randomUser
import kotlin.random.Random

@Entity
data class Chat(
    val message: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var docId: String = ""
    var sentTime: Long = 0
    var fromUserId: String? = null
    var toUserId: String? = null

    companion object {
        fun random(): Chat {
            val chat = Chat(randomName())
            chat.sentTime = System.currentTimeMillis()
            val pair = getRandomFromTo()
            chat.fromUserId = pair.first
            chat.toUserId = pair.second
            return chat
        }
    }
}

@Entity
data class User(val name: String = "") {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var docId: String = ""
    var picture: Int = 0

    companion object {
        fun random(): User {
            val user = User(randomName())
            user.docId = System.currentTimeMillis().toString()
            user.picture = R.drawable.default_user
            return user
        }
    }
}

data class HomeChat(
    val id: String,
    val sender: User,
    val chat: Chat,
    val count: Int,
) {
    companion object {
        fun random(): HomeChat = HomeChat(
            Random.nextInt().toString(),
            randomUser(),
            randomChat(),
            Random.nextInt(from = 0, until = 100)
        )
    }
}