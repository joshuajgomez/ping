package com.joshgm3z.ping.utils

import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import kotlin.random.Random

fun getChatList() = listOf(
    Chat("${Random.nextInt()}", "Hweey guys", randomTime(), randomUser(), null),
    Chat("${Random.nextInt()}", "Hey guddys", randomTime(), null, randomUser()),
    Chat("${Random.nextInt()}", "Hey gudys", randomTime(), null, randomUser()),
    Chat("${Random.nextInt()}", "Hwdey guys", randomTime(), randomUser(), null),
    Chat("${Random.nextInt()}", "Hey gfuys", randomTime(), null, randomUser()),
    Chat("${Random.nextInt()}", "Hewdy guys", randomTime(), randomUser(), null),
)

fun randomUser() = User("${Random.nextInt()}", "User #${Random.nextInt()}", 11)

fun randomTime() = System.currentTimeMillis() + Random.nextLong()