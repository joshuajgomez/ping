package com.joshgm3z.ping.utils

import com.google.firebase.firestore.QuerySnapshot
import com.joshgm3z.ping.data.Chat

class FirestoreConverter {
    companion object {
        private val keyLocalId = "localId"
        private val keyDocId = "docId"
        private val keySentTime = "sentTime"
        private val keyFromUserId = "fromUserId"
        private val keyToUserId = "toUserId"
        private val keyMessage = "message"

        private val defaultDocId = "pending"

        fun getDocumentFromChat(chat: Chat): HashMap<String, Any> {
            return hashMapOf(
                keyLocalId to chat.localId,
                keyDocId to defaultDocId,
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
                chat.docId = document[keyDocId].toString()
                chat.sentTime = document[keySentTime] as Long
                chat.fromUserId = document[keyFromUserId].toString()
                chat.toUserId = document[keyToUserId].toString()
                chatList.add(chat)
            }
            return chatList
        }
    }
}