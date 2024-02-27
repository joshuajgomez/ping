package com.joshgm3z.ping.utils

import com.google.firebase.firestore.QuerySnapshot
import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.User

class FirestoreConverter {
    companion object {
        private const val keySentTime = "sentTime"
        const val keyFromUserId = "fromUserId"
        const val keyToUserId = "toUserId"
        private const val keyMessage = "message"
        const val keyStatus = "status"

        private const val keyName = "name"
        private const val keyImagePath = "imagePath"

        fun getDocumentFromChat(chat: Chat): HashMap<String, Any> {
            return hashMapOf(
                keySentTime to chat.sentTime,
                keyFromUserId to chat.fromUserId,
                keyToUserId to chat.toUserId,
                keyMessage to chat.message,
                keyStatus to chat.status,
            )
        }

        fun getChatListFromDocument(result: QuerySnapshot): ArrayList<Chat> {
            val chatList = ArrayList<Chat>()
            for (document in result) {
                val chat = Chat(message = document[keyMessage].toString())
                chat.docId = document.id
                chat.sentTime = document[keySentTime] as Long
                chat.fromUserId = document[keyFromUserId].toString()
                chat.toUserId = document[keyToUserId].toString()
                chat.status = document[keyStatus] as Long
                chatList.add(chat)
            }
            return chatList
        }

        fun findUserFromDocument(name: String, result: QuerySnapshot): User? {
            for (document in result) {
                if (document[keyName] == name) {
                    val user = User()
                    user.docId = document.id
                    user.name = document[keyName].toString()
                    user.imagePath = document[keyImagePath].toString()
                    return user
                }
            }
            return null
        }

        fun getDocumentFromUser(user: User): HashMap<String, Any> {
            return hashMapOf(
                keyName to user.name,
                keyImagePath to user.imagePath,
            )
        }

        fun getUserListFromDocument(result: QuerySnapshot): ArrayList<User> {
            val userList = ArrayList<User>()
            for (document in result) {
                val user = User(name = document[keyName].toString())
                user.docId = document.id
                user.imagePath = document[keyImagePath].toString()
                userList.add(user)
            }
            return userList
        }
    }
}