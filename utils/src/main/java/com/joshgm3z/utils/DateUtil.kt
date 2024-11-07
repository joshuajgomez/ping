package com.joshgm3z.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getPrettyTime(instance: Long): String {
    val dateFormat = SimpleDateFormat("h.mm a")
    val date = Date(instance)
    return dateFormat.format(date).toLowerCase(Locale.US)
}

fun Long.prettyTime(): String {
    val dateFormat = SimpleDateFormat("h.mm a")
    val date = Date(this)
    return dateFormat.format(date).toLowerCase(Locale.US)
}