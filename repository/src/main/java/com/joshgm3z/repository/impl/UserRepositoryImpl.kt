package com.joshgm3z.repository.impl

import com.joshgm3z.data.model.User
import com.joshgm3z.firebase.FirestoreDb
import com.joshgm3z.repository.api.CurrentUserInfo
import com.joshgm3z.repository.api.UserRepository
import com.joshgm3z.repository.room.PingDb
import com.joshgm3z.repository.room.UserDao
import com.joshgm3z.utils.Logger
import com.joshgm3z.utils.MainThreadScope
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryProvider {
    @Binds
    abstract fun provideUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}

@Singleton
class UserRepositoryImpl
@Inject
constructor(
    private val scope: CoroutineScope,
    @MainThreadScope private val mainScope: CoroutineScope,
    private val db: PingDb,
    private val firestoreDb: FirestoreDb,
    private val currentUserInfo: CurrentUserInfo,
) : UserRepository {
    private val userDao: UserDao
        get() = db.userDao()

    override suspend fun getUsers(): List<User> = userDao.getAll()

    override fun syncUserListWithServer(onUserListUpdated: () -> Unit) {
        Logger.entry()
        firestoreDb.getUserList {
            Logger.debug("it = [$it]")
            if (it.isNotEmpty()) {
                scope.launch {
                    if (currentUserInfo.isSignedIn) {
                        val currentUser = currentUserInfo.currentUser
                        userDao.insertAll(it, currentUser.docId)
                        mainScope.launch {
                            Logger.debug("user list updated")
                            onUserListUpdated()
                        }
                    } else {
                        Logger.warn("current user is null")
                    }

                }
            }
        }
    }

    override fun checkUserInServer(
        name: String,
        onCheckComplete: (user: User) -> Unit,
        onNotFound: () -> Unit,
        onCheckError: () -> Unit,
    ) {
        Logger.info("name=$name")
        firestoreDb.checkUser(
            name,
            onCheckComplete = {
                scope.launch {
                    currentUserInfo.currentUser = it
                    mainScope.launch {
                        onCheckComplete(it)
                    }
                }
            },
            onNotFound = onNotFound,
            onCheckError = onCheckError,
        )

    }

    override fun createUserInServer(
        user: User,
        registerComplete: (name: String) -> Unit,
        onError: (message: String) -> Unit,
    ) {
        Logger.entry
        firestoreDb.createUser(
            user,
            onUserCreated = {
                scope.launch {
                    currentUserInfo.currentUser = it
                    registerComplete(it.name)
                }
            },
            onError = onError
        )

    }

    override suspend fun getUser(userId: String): User =
        userDao.getUser(userId)

    override fun getUserFlow(userId: String): Flow<User> =
        userDao.getUserFlow(userId)

    override suspend fun signOutUser() {
        Logger.entry()
        currentUserInfo.removeCurrentUser()
        firestoreDb.removeChatListener()
        db.clearAllTables()
    }

    override suspend fun updateUserInfo(
        key: String,
        value: String,
        onUpdated: () -> Unit,
        onError: (String) -> Unit,
    ) {
        Logger.debug("key = [${key}], value = [${value}]")
        firestoreDb.updateUserInfo(
            userId = currentUserInfo.currentUser.docId,
            key = key,
            value = value,
            onUpdateComplete = {
                currentUserInfo.currentUser = it
                mainScope.launch {
                    onUpdated()
                }
            },
            onUpdateError = onError
        )
    }
}