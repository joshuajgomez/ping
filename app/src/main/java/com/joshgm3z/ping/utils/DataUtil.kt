package com.joshgm3z.ping.utils

import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.HomeChat
import com.joshgm3z.ping.data.User

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
            Logger.debug("chats = [$chats]")
            val homeChats: HashMap<String, HomeChat> = HashMap()
            for (chat in chats) {
                val user = getUser(chat.fromUserId, users)
                var homeChat: HomeChat
                if (homeChats.containsKey(user.docId)) {
                    homeChat = homeChats[user.docId]!!
                    homeChat.count++
                } else {
                    homeChat = HomeChat()
                    homeChat.otherGuy = user
                    homeChat.count = 1
                }
                homeChat.lastChat = chat
                homeChats[user.docId] = homeChat
            }
            Logger.debug("homeChats = [$homeChats]")
            return ArrayList(homeChats.values)
        }

        private fun getUser(fromUserId: String, users: List<User>): User {
            Logger.debug("fromUserId = [${fromUserId}], users = [${users}]")
            for (user in users) {
                if (user.docId == fromUserId) {
                    return user
                }
            }
            return User()
        }
    }

}
