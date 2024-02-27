package com.joshgm3z.ping.model.firestore

import com.google.firebase.Firebase
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.joshgm3z.ping.model.data.Chat
import com.joshgm3z.ping.model.data.User
import com.joshgm3z.ping.utils.FirebaseLogger
import com.joshgm3z.ping.utils.FirestoreConverter
import com.joshgm3z.ping.utils.Logger

class FirestoreDb(private val firebaseLogger: FirebaseLogger) {

    private val firestore = Firebase.firestore

    private val keyCollectionChatList = "chatList"
    private val keyCollectionUserList = "userList"

    private lateinit var listenerRegistration: ListenerRegistration
    private var onChatReceived: ((List<Chat>) -> Unit)? = null

    fun registerChat(
        chat: Chat,
        onIdSet: (id: String) -> Unit,
        onError: () -> Unit,
    ) {
        Logger.debug("chat = [${chat}]")
        val chatDoc = FirestoreConverter.getDocumentFromChat(chat)
        firestore.collection(keyCollectionChatList)
            .add(chatDoc)
            .addOnSuccessListener {
                Logger.debug("chat <${chat}> added with id: ${it.id}")
                onIdSet(it.id)
            }
            .addOnFailureListener {
                Logger.warn("error adding chat: ${it.message}")
                onError()
            }
    }

    fun checkUser(name: String, onCheckComplete: (user: User?) -> Unit) {
        firebaseLogger.debug("checking user for sign in: $name")
        firestore.collection(keyCollectionUserList)
            .get()
            .addOnSuccessListener { result ->
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
        firestore.collection(keyCollectionUserList)
            .add(userDoc)
            .addOnSuccessListener {
                Logger.debug("user created: ${user.name} with id: ${it.id}")
                firebaseLogger.debug("user created: ${user.name} with id: ${it.id}")
                user.docId = it.id
                onUserCreated(user, "")
            }
            .addOnFailureListener {
                Logger.error("unable to create user: ${it.message}")
                onUserCreated(null, it.message.toString())
            }
    }

    fun getUserList(onUserListFetched: (userList: List<User>) -> Unit) {
        firestore.collection(keyCollectionUserList)
            .get()
            .addOnSuccessListener {
                val users = FirestoreConverter.getUserListFromDocument(it)
                onUserListFetched(users)
            }
            .addOnFailureListener {
                Logger.warn("error fetching user list: ${it.message}")
            }
    }

    fun listenForChatToOrFromUser(
        userId: String,
        chatListener: (chats: List<Chat>) -> Unit,
    ) {
        Logger.info("userId = [${userId}]")
        onChatReceived = chatListener
        listenerRegistration = firestore.collection(keyCollectionChatList).where(
            Filter.or(
                Filter.equalTo(FirestoreConverter.keyToUserId, userId),
                Filter.equalTo(FirestoreConverter.keyFromUserId, userId),
            )
        ).addSnapshotListener(eventListener)
    }

    private val eventListener = EventListener { value: QuerySnapshot?, error: Throwable? ->
        if (value != null) {
            val chats = FirestoreConverter.getChatListFromDocument(value)
            onChatReceived?.let { it(chats) }
        } else {
            Logger.warn(error?.message.toString())
        }
    }

    fun removeChatListener() {
        onChatReceived = null
        listenerRegistration.remove()
    }

    fun updateChatStatus(chat: Chat) {
        Logger.debug("chat = [$chat]")
        firestore.collection(keyCollectionChatList)
            .document(chat.docId)
            .update(FirestoreConverter.keyStatus, chat.status)
            .addOnSuccessListener {}
            .addOnFailureListener {
                Logger.warn("error updating chat status: $chat")
            }
    }

}