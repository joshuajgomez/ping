package com.joshgm3z.ping.model.firestore

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.joshgm3z.ping.data.Chat
import com.joshgm3z.ping.utils.FirestoreConverter

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
}