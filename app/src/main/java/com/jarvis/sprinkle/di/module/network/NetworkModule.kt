package com.jarvis.sprinkle.di.module.network

import com.jarvis.sprinkle.di.scope.SprinkleAppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {
    companion object {
        private const val CONNECTION_TIMEOUT: Long = 60
        private const val WRITE_TIMEOUT: Long = 60
        private const val READ_TIMEOUT: Long = 60
    }


    @Provides
    @SprinkleAppScope
    fun okHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @SprinkleAppScope
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message -> Timber.i(message) }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }
}