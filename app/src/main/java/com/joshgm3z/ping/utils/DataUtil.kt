package com.joshgm3z.ping.utils

import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.HomeChat
import com.joshgm3z.ping.model.data.User

class DataUtil {
    companion object {
        fun markOutwardChats(meUserId: String, chats: ArrayList<Chat>): List<Chat> {
            for (chat in chats) {
                if (chat.fromUserId != meUserId) {
                    chat.isOutwards = false
                }
            }
            return chats
        }

        fun buildHomeChats(chats: List<Chat>, users: List<User>): List<HomeChat> {
            val homeChats: HashMap<String, HomeChat> = HashMap()
            for (chat in chats) {
                val user = getUser(chat.fromUserId, users)
                var homeChat: HomeChat
                if (homeChats.containsKey(user.docId)) {
                    homeChat = homeChats[user.docId]!!
                } else {
                    homeChat = HomeChat()
                    homeChat.otherGuy = user
                }
                if (chat.status == Chat.DELIVERED) homeChat.count++
                homeChat.lastChat = chat
                homeChats[user.docId] = homeChat
            }
            return ArrayList(homeChats.values)
        }

        private fun getUser(fromUserId: String, users: List<User>): User {
            for (user in users) {
                if (user.docId == fromUserId) {
                    return user
                }
            }
            return User()
        }
    }

}
