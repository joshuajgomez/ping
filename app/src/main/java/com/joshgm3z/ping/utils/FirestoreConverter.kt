package com.joshgm3z.ping.utils

import com.google.firebase.firestore.QuerySnapshot
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User

class FirestoreConverter {
    companion object {
        val keyLocalId = "localId"
        val keySentTime = "sentTime"
        val keyFromUserId = "fromUserId"
        val keyToUserId = "toUserId"
        val keyMessage = "message"
        val keyName = "name"
        val keyImagePath = "imagePath"

        private val defaultDocId = "pending"

        fun getDocumentFromChat(chat: Chat): HashMap<String, Any> {
            return hashMapOf(
                keyLocalId to chat.localId,
                keySentTime to chat.sentTime,
                keyFromUserId to "${chat.fromUserId}",
                keyToUserId to "${chat.toUserId}",
                keyMessage to chat.message,
            )
        }

        fun getChatListFromDocument(result: QuerySnapshot): ArrayList<Chat> {
            val chatList = ArrayList<Chat>()
            for (document in result) {
                val chat = Chat(message = document[keyMessage].toString())
                chat.localId = document[keyLocalId] as Long
                chat.docId = document.id
                chat.sentTime = document[keySentTime] as Long
                chat.fromUserId = document[keyFromUserId].toString()
                chat.toUserId = document[keyToUserId].toString()
                chatList.add(chat)
            }
            return chatList
        }

        fun findUserFromDocument(name: String, result: QuerySnapshot): User? {
            Logger.info("result = [${result}]")
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