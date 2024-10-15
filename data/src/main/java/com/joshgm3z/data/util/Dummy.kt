package com.joshgm3z.data.util

import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.HomeChat
import com.joshgm3z.data.model.User
import kotlin.random.Random

fun getChatList() = listOf(
    Chat.random(),
    Chat.random(),
    Chat.random(),
    Chat.random(),
    Chat.random(),
    Chat.random(),
    Chat.random(),
    Chat.random(),
)

fun getRandomFromTo(): Pair<String, String> {
    return listOf(
        Pair("", "some"),
        Pair("", "user"),
        Pair("is", ""),
        Pair("", "in the"),
        Pair("run", ""),
    ).random()
}

fun getHomeChatList() = listOf(
    HomeChat.random(),
    HomeChat.random(),
    HomeChat.random(),
    HomeChat.random(),
    HomeChat.random(),
).sortedBy { homeChat -> homeChat.lastChat.sentTime }

fun randomChat() = getChatList().random()

fun randomUser() = User.random()

fun randomUsers() = listOf(
    User.random(),
    User.random(),
    User.random(),
    User.random(),
    User.random(),
)

fun randomTime() = System.currentTimeMillis() + Random.nextLong()

fun randomName() =
    listOf("Gandalf", "Samwise", "Frodo", "Bilbo", "Araghon", "Sauron", "Sarumon").random()

fun randomAbout() =
    listOf(
        "Peace for us all",
        "Something fishy going on",
        "Just joined ping",
        "New here. whats up",
        "He he ha ha !!",
    ).random()


fun randomMessage() =
    listOf(
        "Hey you guys",
        "Wassup maan",
        "Da",
        "yyooo",
        "I just wanted to confirm one thing with you real something something",
        "I just wanted to confirm one thing with you real something something",
        "I just wanted to confirm one thing with you real something something",
        "I just wanted to confirm one thing with you real something something, I just wanted to confirm one thing with you real something something, I just wanted to confirm one thing with you real something something, I just wanted to confirm one thing with you real something something, ",
        "Ok ok",
        "."
    ).random()