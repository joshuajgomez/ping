package com.joshgm3z.ping

import android.app.Application
import android.content.Context
import android.content.Intent
import com.joshgm3z.utils.NotificationUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context

    @Provides
    fun provideScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)
}

@HiltAndroidApp
class PingApp : Application() {

    @Inject
    lateinit var notificationUtil: NotificationUtil

    override fun onCreate() {
        super.onCreate()
        updateIntent()
    }

    private fun updateIntent() {
        Intent(applicationContext, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            notificationUtil.intent = this
        }
    }
}