package com.joshgm3z.ping.data

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
)
