package com.joshgm3z.ping.utils

import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.HomeChat
import com.joshgm3z.ping.data.User
import kotlin.random.Random

fun getChatList() = listOf(
    Chat(Random.nextInt().toString(), randomMessage(), randomTime(), randomUser(), null),
    Chat(Random.nextInt().toString(), randomMessage(), randomTime(), randomUser(), null),
    Chat(Random.nextInt().toString(), randomMessage(), randomTime(), null, randomUser()),
    Chat(Random.nextInt().toString(), randomMessage(), randomTime(), randomUser(), null),
    Chat(Random.nextInt().toString(), randomMessage(), randomTime(), null, randomUser()),
)

fun getHomeChatList() = listOf(
    HomeChat.random(),
    HomeChat.random(),
    HomeChat.random(),
    HomeChat.random(),
    HomeChat.random(),
)

fun randomChat() = getChatList().random()

fun randomUser() = User.random()

fun randomTime() = System.currentTimeMillis() + Random.nextLong()

fun randomName() =
    listOf("Gandalf", "Samwise", "Frodo", "Bilbo", "Araghon", "Sauron", "Sarumon").random()

fun randomMessage() =
    listOf(
        "Hey you guys",
        "Wassup maan",
        "Da",
        "yyooo",
        "I just wanted to confirm one thing with you real something something",
        "I just wanted to confirm one thing with you real something something",
        "I just wanted to confirm one thing with you real something something",
        "I just wanted to confirm one thing with you real something something",
        "Ok ok",
        "."
    ).random()