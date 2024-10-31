package com.joshgm3z.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.utils.Logger
import com.joshgm3z.utils.const.FirestoreKey

class FirestoreConverter {
    companion object {

        fun getDocumentFromChat(chat: Chat): HashMap<String, Any> {
            return hashMapOf(
                FirestoreKey.Chat.sentTime to chat.sentTime,
                FirestoreKey.Chat.fromUserId to chat.fromUserId,
                FirestoreKey.Chat.toUserId to chat.toUserId,
                FirestoreKey.Chat.message to chat.message,
                FirestoreKey.Chat.imageUrl to chat.imageUrl,
                FirestoreKey.Chat.status to chat.status,
                FirestoreKey.Chat.replyToChatId to chat.replyToChatId,
            )
        }

        fun getChatListFromDocument(result: QuerySnapshot): ArrayList<Chat> {
            val chatList = ArrayList<Chat>()
            result.forEach {
                with(it) {
                    try {
                        Chat(message = get(FirestoreKey.Chat.message).toString()).apply {
                            docId = id
                            sentTime = get(FirestoreKey.Chat.sentTime) as Long
                            fromUserId = get(FirestoreKey.Chat.fromUserId).toString()
                            toUserId = get(FirestoreKey.Chat.toUserId).toString()
                            status = get(FirestoreKey.Chat.status) as Long
                            imageUrl = get(FirestoreKey.Chat.imageUrl).toString()
                            replyToChatId = get(FirestoreKey.Chat.replyToChatId).toString()
                            chatList.add(this)
                        }
                    } catch (e: Exception) {
                        Logger.debug(e.stackTraceToString())
                    }
                }
            }
            return chatList
        }

        fun getUserFromDocument(result: DocumentSnapshot): User = with(result) {
            User().apply {
                docId = id
                name = get(FirestoreKey.User.name).toString()
                imagePath = get(FirestoreKey.User.imagePath).toString()
                about = get(FirestoreKey.User.about).toString()
                get(FirestoreKey.User.dateOfJoining)?.let { dateOfJoining = it as Long }
                get(FirestoreKey.User.profileIcon)?.let {
                    profileIcon = Integer.parseInt(it.toString())
                }
            }
        }


        fun findUserFromDocument(userName: String, result: QuerySnapshot): User? {
            val document = result.firstOrNull {
                it.get(FirestoreKey.User.name) == userName
            }
            return when {
                document == null -> null
                else -> getUserFromDocument(document)

            }
        }

        fun getDocumentFromUser(user: User): HashMap<String, Any> {
            return hashMapOf(
                FirestoreKey.User.name to user.name,
                FirestoreKey.User.imagePath to user.imagePath,
                FirestoreKey.User.about to user.about,
                FirestoreKey.User.dateOfJoining to user.dateOfJoining,
                FirestoreKey.User.profileIcon to user.profileIcon,
            )
        }

        fun getUserListFromDocument(result: QuerySnapshot): ArrayList<User> {
            val userList = ArrayList<User>()
            for (document in result) {
                userList.add(getUserFromDocument(document))
            }
            return userList
        }

    }
}
