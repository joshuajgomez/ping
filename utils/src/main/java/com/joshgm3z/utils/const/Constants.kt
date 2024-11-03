package com.joshgm3z.utils.const

const val OPEN_CHAT_USER: String = "open_chat_user_id"

class FirestoreKey {
    class Chat {
        companion object {
            const val fromUserId = "fromUserId"
            const val toUserId = "toUserId"
            const val message = "message"
            const val imageUrl = "imageUrl"
            const val status = "status"
            const val sentTime = "sentTime"
            const val replyToChatId = "replyToChatId"
            const val fileName = "fileName"
            const val fileSize = "fileSize"
            const val fileType = "fileType"
        }
    }

    class User {
        companion object {
            const val name = "name"
            const val imagePath = "imagePath"
            const val profileIcon = "profileIcon"
            const val about = "about"
            const val dateOfJoining = "dateOfJoining"
        }
    }

    companion object {
        val keyChatFiles = "chat_files"
        val keyProfilePicture = "profile_pictures"
    }
}