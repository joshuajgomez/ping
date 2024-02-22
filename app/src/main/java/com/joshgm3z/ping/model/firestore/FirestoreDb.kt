package com.joshgm3z.ping.model.firestore

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.joshgm3z.ping.data.Chat

class FirestoreDb {

    private val TAG = "FirestoreDb"
    val db = Firebase.firestore

    fun registerChat(
        chat: Chat,
        onIdSet: (id: String) -> Unit,
        onError: () -> Unit,
    ) {
        val chatDoc = hashMapOf(
            "id" to chat.id,
            "docId" to "pending",
            "sentTime" to chat.sentTime,
            "fromUserId" to chat.fromUserId,
            "toUserId" to chat.toUserId,
            "message" to chat.message,
        )
        db.collection("chatList")
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
}