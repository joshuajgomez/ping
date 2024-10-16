package com.joshgm3z.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.joshgm3z.data.util.randomAbout
import com.joshgm3z.data.util.randomName

@Entity
data class User(var name: String = "") {
    @PrimaryKey
    var docId: String = ""
    var imagePath: String = ""
    var about: String = randomAbout()
    var dateOfJoining: Long = 0

    companion object {
        fun random(): User {
            val user = User(randomName())
            user.docId = System.currentTimeMillis().toString()
            user.imagePath = ""
            user.about = randomAbout()
            user.dateOfJoining = 1729003470279 // October 15, 2025 4.30pm
            return user
        }
    }

    override fun toString(): String {
        return "User(name='$name', docId='$docId', imagePath=$imagePath, about=$about, dateOfJoining=$dateOfJoining)"
    }

}