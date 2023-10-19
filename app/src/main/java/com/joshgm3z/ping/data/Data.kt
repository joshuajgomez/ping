package com.joshgm3z.ping.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.joshgm3z.ping.R
import com.joshgm3z.ping.utils.randomChat
import com.joshgm3z.ping.utils.randomName
import com.joshgm3z.ping.utils.randomUser
import kotlin.random.Random

@Entity
data class Chat(
    @PrimaryKey
    val id: String,
    val message: String,
    val sentTime: Long,
    val fromUserId: String?,
    val toUserId: String?,
)

@Entity
data class User(
    @PrimaryKey
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