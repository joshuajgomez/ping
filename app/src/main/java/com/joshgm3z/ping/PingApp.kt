package com.joshgm3z.ping

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context

    @Provides
    fun provideScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)
}

@HiltAndroidApp
class PingApp : Application()