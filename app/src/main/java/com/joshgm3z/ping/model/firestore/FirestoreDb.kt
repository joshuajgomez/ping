package com.joshgm3z.ping.model.firestore

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.data.User
import com.joshgm3z.ping.utils.FirestoreConverter
import com.joshgm3z.ping.utils.Logger

class FirestoreDb {

    private val TAG = "FirestoreDb"
    private val db = Firebase.firestore

    private val keyCollectionChatList = "chatList"
    private val keyCollectionUserList = "userList"

    fun registerChat(
        chat: Chat,
        onIdSet: (id: String) -> Unit,
        onError: () -> Unit,
    ) {
        val chatDoc = FirestoreConverter.getDocumentFromChat(chat)
        db.collection(keyCollectionChatList)
            .add(chatDoc)
            .addOnSuccessListener {
                Log.d(TAG, "chat added with id: ${it.id}")
                onIdSet(it.id)
            }
            .addOnFailureListener {
                Log.w(TAG, "error adding chat: ${it.message}")
                onError()
            }
    }

    fun observeChatList() {
        db.collection(keyCollectionChatList)
            .get()
            .addOnSuccessListener { result ->
                val chatList = FirestoreConverter.getChatListFromDocument(result)
                Log.i("FireStoreConverter", chatList.toString())
            }
    }

    fun checkUser(name: String, onCheckComplete: (user: User?) -> Unit) {
        db.collection(keyCollectionUserList)
            .get()
            .addOnSuccessListener { result ->
                Logger.debug("user list received: ${result.size()}")
                var user: User? = null
                if (!result.isEmpty)
                    user = FirestoreConverter.findUserFromDocument(name, result)
                onCheckComplete(user)
            }
            .addOnFailureListener {
                Logger.error(it.message.toString())
                onCheckComplete(null)
            }
    }

    fun createUser(user: User, onUserCreated: (user: User?, message: String) -> Unit) {
        val userDoc = FirestoreConverter.getDocumentFromUser(user)
        db.collection(keyCollectionUserList)
            .add(userDoc)
            .addOnSuccessListener {
                Logger.debug("user created: ${user.name} with id: ${it.id}")
                user.docId = it.id
                onUserCreated(user, "")
            }
            .addOnFailureListener {
                Logger.error("unable to create user: ${it.message}")
                onUserCreated(null, it.message.toString())
            }
    }
}