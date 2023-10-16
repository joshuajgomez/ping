package com.joshgm3z.ping.data

import com.joshgm3z.ping.R
import com.joshgm3z.ping.utils.randomChat
import com.joshgm3z.ping.utils.randomName
import com.joshgm3z.ping.utils.randomUser
import kotlin.random.Random

data class Chat(
    val id: String,
    val message: String,
    val sentTime: Long,
    val fromUser: User?,
    val toUser: User?,
)

data class User(
    val id: String,
    val name: String,
    val picture: Int,
) {
    companion object {
        fun random(): User = User(
            Random.nextInt().toString(),
            randomName(),
            R.drawable.default_user
        )
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