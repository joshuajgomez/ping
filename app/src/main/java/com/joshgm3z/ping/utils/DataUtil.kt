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

        fun buildHomeChats(meUserId: String, chats: List<Chat>, users: List<User>): List<HomeChat> {
            val homeChats: HashMap<String, HomeChat> = HashMap()
            for (chat in chats) {
                Logger.debug("chat = [$chat]")
                val user = getUser(chat.fromUserId, chat.toUserId, users)
                var homeChat: HomeChat
                if (homeChats.containsKey(user.docId)) {
                    homeChat = homeChats[user.docId]!!
                } else {
                    homeChat = HomeChat()
                    homeChat.otherGuy = user
                    homeChat.count = 0
                }
                if (chat.status == Chat.DELIVERED) homeChat.count++
                if (chat.fromUserId != meUserId) chat.isOutwards = false
                homeChat.lastChat = chat

                homeChats[user.docId] = homeChat
            }
            Logger.debug("homeChats = [$homeChats]")
            val arrayList = ArrayList(homeChats.values)
            arrayList.sortByDescending { homeChat -> homeChat.lastChat.sentTime }
            return arrayList
        }

        private fun getUser(fromUserId: String, toUserId: String, users: List<User>): User {
            for (user in users) {
                if (user.docId == fromUserId || user.docId == toUserId) {
                    return user
                }
            }
            return User()
        }
    }

}
