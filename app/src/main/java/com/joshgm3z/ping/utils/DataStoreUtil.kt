package com.joshgm3z.ping.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.joshgm3z.ping.model.data.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreUtil
@Inject
constructor(
    private val context: Context
) {

    private val dataStoreName = "ping_data_store"

    private val keyUserName = stringPreferencesKey("userName")
    private val keyUserDocId = stringPreferencesKey("userDocId")
    private val keyUserImagePath = stringPreferencesKey("userImagePath")

    private val Context.dataStore by preferencesDataStore(name = dataStoreName)

    suspend fun setUser(user: User) {
        Logger.debug("user = [$user]")
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[keyUserName] = user.name
            mutablePreferences[keyUserDocId] = user.docId
            mutablePreferences[keyUserImagePath] = user.imagePath
        }
    }

    fun isUserSignedIn(): Boolean = runBlocking {
        val isUserSignedIn = context.dataStore.data.first().contains(keyUserName)
        Logger.debug("$isUserSignedIn")
        isUserSignedIn
    }

    suspend fun getCurrentUser(): User {
        val preferences = context.dataStore.data.first()
        val user = User(preferences[keyUserName].toString())
        user.docId = preferences[keyUserDocId].toString()
        user.imagePath = preferences[keyUserImagePath].toString()
        return user
    }

    suspend fun removeCurrentUser() {
        Logger.entry()
        context.dataStore.edit { mutablePreferences -> mutablePreferences.clear() }
    }
}
