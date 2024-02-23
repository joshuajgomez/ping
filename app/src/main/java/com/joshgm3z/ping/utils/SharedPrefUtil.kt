package com.joshgm3z.ping.utils

import android.content.Context
import com.joshgm3z.ping.PingApp
import com.joshgm3z.ping.data.User

class SharedPrefUtil {

    companion object {

        private const val sharedPrefsName = "ping_shared_preferences"
        private const val keyUserName = "userName"
        private const val keyUserDocId = "userDocId"
        private const val keyUserImagePath = "imagePath"

        private val sharedPrefs =
            PingApp.getContext().getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE)

        fun setUser(user: User) {
            val edit = sharedPrefs.edit()
            edit.putString(keyUserName, user.name)
            edit.putString(keyUserDocId, user.docId)
            edit.putString(keyUserImagePath, user.imagePath)
            edit.apply()
        }

        fun isUserSignedIn(): Boolean {
            return sharedPrefs.getString(keyUserName, "")?.isNotEmpty() ?: false
        }
    }
}
