package com.joshgm3z.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.joshgm3z.data.R
import com.joshgm3z.data.util.randomName

@Entity
data class User(var name: String = "") {
    @PrimaryKey
    var docId: String = ""
    var imagePath: String = ""

    companion object {
        fun random(): User {
            val user = User(randomName())
            user.docId = System.currentTimeMillis().toString()
            user.imagePath = ""
            return user
        }
    }

    override fun toString(): String {
        return "User(name='$name', docId='$docId', imagePath=$imagePath)"
    }

    val imageRes: Int
        get() {
            return if (imagePath.isNotEmpty() && imagePath != "null")
                imagePath.toInt()
            else R.drawable.default_user
        }
}