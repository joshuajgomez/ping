package com.joshgm3z.ping.utils

import com.joshgm3z.ping.data.Chat

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
    }

}
