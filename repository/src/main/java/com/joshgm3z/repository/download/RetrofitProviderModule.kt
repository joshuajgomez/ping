package com.joshgm3z.repository.download

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitProviderModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://example.com/").build()
    }

    @Provides
    @Singleton
    fun provideDownloadService(retrofit: Retrofit): DownloadService {
        return retrofit.create(DownloadService::class.java)
    }

}

interface DownloadService {
    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody
}