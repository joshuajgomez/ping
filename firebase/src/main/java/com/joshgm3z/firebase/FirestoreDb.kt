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
import com.joshgm3z.utils.const.FirestoreKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreDb
@Inject
constructor(
    private val firebaseLogger: FirebaseLogger
) {
    private val firestore = Firebase.firestore

    private val keyCollectionChatList = "chats_list"
    private val keyCollectionUserList = "users_list"

    private lateinit var listenerRegistration: ListenerRegistration
    private var onChatReceived: ((List<Chat>) -> Unit)? = null

    fun createChatDocId(): String =
        firestore.collection(keyCollectionChatList).document().id

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

    fun registerChatWithId(
        chat: Chat,
        onError: () -> Unit,
        onSuccess: () -> Unit,
    ) {
        Logger.debug("chat = [${chat}]")
        val chatDoc = FirestoreConverter.getDocumentFromChat(chat)
        firestore.collection(keyCollectionChatList)
            .document(chat.docId)
            .set(chatDoc)
            .addOnSuccessListener {
                Logger.debug("chat <${chat}> added with id")
                onSuccess()
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
                Filter.equalTo(FirestoreKey.Chat.toUserId, userId),
                Filter.equalTo(FirestoreKey.Chat.fromUserId, userId),
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

    fun updateUserInfo(
        userId: String,
        key: String,
        value: String,
        onUpdateComplete: (User) -> Unit,
        onUpdateError: (String) -> Unit,
    ) {
        Logger.debug("userId = [${userId}], key = [${key}], value = [${value}]")
        val doc = firestore.collection(keyCollectionUserList).document(userId)
        doc.update(key, value)
            .addOnSuccessListener {
                Logger.debug("success1")
                doc.get().addOnSuccessListener {
                    val user = FirestoreConverter.getUserFromDocument(it)
                    Logger.debug("success $user")
                    onUpdateComplete(user)
                }.addOnFailureListener {
                    Logger.error("failure ${it.message}")
                    onUpdateError(it.message.toString())
                }
            }.addOnFailureListener {
                Logger.error("failure ${it.message}")
                onUpdateError(it.message.toString())

            }
    }

    fun updateChat(chatId: String, key: String, value: Any) {
        Logger.debug("chatId = [${chatId}], key = [${key}], value = [${value}]")
        firestore.collection(keyCollectionChatList)
            .document(chatId)
            .update(key, value)
            .addOnSuccessListener {}
            .addOnFailureListener {
                Logger.warn("error updating chat status: $chatId")
            }
    }
}
