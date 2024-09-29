package com.joshgm3z.firebase

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseLogger
@Inject constructor(
    context: Context
) {
    val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun debug(message: String) {
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
//            param(FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAMEITEM_ID, "id")
//            param(FirebaseAnalytics.Param.ITEM_NAME, "name")
//            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
//        }
    }

}
