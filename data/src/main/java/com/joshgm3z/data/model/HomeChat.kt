package com.joshgm3z.data.model

import kotlin.random.Random

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