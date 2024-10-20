package com.joshgm3z.firebase

import com.google.firebase.firestore.QuerySnapshot
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User

class FirestoreConverter {
    companion object {
        private const val keySentTime = "sentTime"
        const val keyFromUserId = "fromUserId"
        const val keyToUserId = "toUserId"
        private const val keyMessage = "message"
        private const val keyImageUrl = "imageUrl"
        const val keyStatus = "status"

        private const val keyName = "name"
        const val keyImagePath = "imagePath"
        const val keyProfileIcon = "profileIcon"
        const val keyAbout = "about"
        const val keyDateOfJoining = "dateOfJoining"

        fun getDocumentFromChat(chat: Chat): HashMap<String, Any> {
            return hashMapOf(
                keySentTime to chat.sentTime,
                keyFromUserId to chat.fromUserId,
                keyToUserId to chat.toUserId,
                keyMessage to chat.message,
                keyImageUrl to chat.imageUrl,
                keyStatus to chat.status,
            )
        }

        fun getChatListFromDocument(result: QuerySnapshot): ArrayList<Chat> {
            val chatList = ArrayList<Chat>()
            for (document in result) {
                Chat(message = document[keyMessage].toString()).apply {
                    docId = document.id
                    sentTime = document[keySentTime] as Long
                    fromUserId = document[keyFromUserId].toString()
                    toUserId = document[keyToUserId].toString()
                    status = document[keyStatus] as Long
                    imageUrl = document[keyImageUrl].toString()
                    chatList.add(this)
                }
            }
            return chatList
        }

        fun findUserFromDocument(userName: String, result: QuerySnapshot): User? {
            for (document in result) {
                if (document[keyName] == userName) {
                    User().apply {
                        docId = document.id
                        name = document[keyName].toString()
                        imagePath = document[keyImagePath].toString()
                        about = document[keyAbout].toString()
                        document[keyDateOfJoining]?.let { dateOfJoining = it as Long }
                        document[keyProfileIcon]?.let { profileIcon = Integer.parseInt(it.toString()) }
                        return this
                    }
                }
            }
            return null
        }

        fun getDocumentFromUser(user: User): HashMap<String, Any> {
            return hashMapOf(
                keyName to user.name,
                keyImagePath to user.imagePath,
                keyAbout to user.about,
                keyDateOfJoining to user.dateOfJoining,
                keyProfileIcon to user.profileIcon,
            )
        }

        fun getUserListFromDocument(result: QuerySnapshot): ArrayList<User> {
            val userList = ArrayList<User>()
            for (document in result) {
                User(name = document[keyName].toString()).apply {
                    docId = document.id
                    imagePath = document[keyImagePath].toString()
                    about = document[keyAbout]?.toString() ?: ""
                    document[keyDateOfJoining]?.let { dateOfJoining = it as Long }
                    document[keyProfileIcon]?.let { profileIcon = Integer.parseInt(it.toString()) }
                    userList.add(this)
                }
            }
            return userList
        }

    }
}
