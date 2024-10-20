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
    private val keyUserProfileIcon = stringPreferencesKey("userProfileIcon")
    private val keyUserAbout = stringPreferencesKey("userAbout")
    private val keyUserDateOfJoining = stringPreferencesKey("userDateOfJoining")

    private val Context.dataStore by preferencesDataStore(name = dataStoreName)

    fun setUser(user: User) = runBlocking {
        Logger.debug("user = [$user]")
        context.dataStore.edit { mutablePreferences ->
            mutablePreferences[keyUserName] = user.name
            mutablePreferences[keyUserDocId] = user.docId
            mutablePreferences[keyUserImagePath] = user.imagePath
            mutablePreferences[keyUserAbout] = user.about
            mutablePreferences[keyUserDateOfJoining] = user.dateOfJoining.toString()
            mutablePreferences[keyUserProfileIcon] = user.profileIcon.toString()
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
        user.about = preferences[keyUserAbout].toString()
        user.dateOfJoining = preferences[keyUserDateOfJoining]?.toLong() ?: 0
        user.profileIcon = preferences[keyUserProfileIcon]?.toInt() ?: 0
        return@runBlocking user
    }

    fun removeCurrentUser() = runBlocking {
        Logger.entry()
        context.dataStore.edit { mutablePreferences -> mutablePreferences.clear() }
    }
}
