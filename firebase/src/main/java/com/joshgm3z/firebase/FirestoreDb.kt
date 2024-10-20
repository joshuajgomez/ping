package com.joshgm3z.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.joshgm3z.data.model.Chat
import com.joshgm3z.data.model.User
import com.joshgm3z.utils.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreDb
@Inject
constructor(
    private val firebaseLogger: FirebaseLogger
) {
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

    fun checkUser(
        name: String,
        onCheckComplete: (user: User) -> Unit,
        onNotFound: () -> Unit,
        onCheckError: () -> Unit,
    ) {
        firebaseLogger.debug("checking user for sign in: $name")
        Logger.debug("name = [${name}]")
        firestore.collection(keyCollectionUserList)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val user = FirestoreConverter.findUserFromDocument(name, result)
                    if (user != null) {
                        onCheckComplete(user)
                    } else {
                        onNotFound()
                    }
                } else {
                    onNotFound()
                }
            }
            .addOnFailureListener {
                Logger.error(it.message.toString())
                onCheckError()
            }
    }

    fun createUser(
        user: User,
        onUserCreated: (user: User) -> Unit,
        onError: (message: String) -> Unit,
    ) {
        Logger.debug("user = [${user}]")
        val userDoc = FirestoreConverter.getDocumentFromUser(user)
        firestore.collection(keyCollectionUserList)
            .add(userDoc)
            .addOnSuccessListener {
                Logger.debug("user created: ${user.name} with id: ${it.id}")
                firebaseLogger.debug("user created: ${user.name} with id: ${it.id}")
                user.docId = it.id
                onUserCreated(user)
            }
            .addOnFailureListener {
                Logger.error("unable to create user: ${it.message}")
                onError(it.message.toString())
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

    fun updateUserImage(
        user: User,
        onUpdateComplete: (user: User) -> Unit,
        onUpdateError: (String) -> Unit,
    ) {
        firestore.collection(keyCollectionUserList)
            .document(user.docId)
            .update(FirestoreConverter.keyImagePath, user.imagePath)
            .addOnSuccessListener {
                onUpdateComplete(user)
            }
            .addOnFailureListener {
                onUpdateError(it.message.toString())
            }
    }

    fun updateUserIcon(
        user: User,
        onUpdateComplete: () -> Unit,
        onUpdateError: (String) -> Unit,
    ) {
        Logger.debug("user = [${user}]]")
        firestore.collection(keyCollectionUserList)
            .document(user.docId)
            .update(FirestoreConverter.keyProfileIcon, user.profileIcon)
            .addOnSuccessListener {
                Logger.debug("success $user")
                onUpdateComplete()
            }
            .addOnFailureListener {
                Logger.debug("failure $user")
                onUpdateError(it.message.toString())
            }
    }

}