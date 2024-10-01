package com.joshgm3z.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.joshgm3z.data.model.User
import com.joshgm3z.utils.Logger
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

    fun setUser(user: User) = runBlocking {
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

    fun getCurrentUser(): User = runBlocking {
        val preferences = context.dataStore.data.first()
        val user = User(preferences[keyUserName].toString())
        user.docId = preferences[keyUserDocId].toString()
        user.imagePath = preferences[keyUserImagePath].toString()
        return@runBlocking user
    }

    fun removeCurrentUser() = runBlocking {
        Logger.entry()
        context.dataStore.edit { mutablePreferences -> mutablePreferences.clear() }
    }
}
