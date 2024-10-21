package com.joshgm3z.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainThreadScope

@Module
@InstallIn(SingletonComponent::class)
class ScopeProvider {
    @MainThreadScope
    @Provides
    fun provideMainScope(): CoroutineScope = CoroutineScope(Dispatchers.Main)

    @Provides
    fun provideScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)
}