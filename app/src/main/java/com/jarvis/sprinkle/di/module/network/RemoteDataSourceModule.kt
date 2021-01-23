package com.jarvis.sprinkle.di.module.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jarvis.sprinkle.BuildConfig
import com.jarvis.sprinkle.data.network.service.ProductService
import com.jarvis.sprinkle.di.scope.SprinkleAppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [NetworkModule::class])
class RemoteDataSourceModule {

    @Provides
    @SprinkleAppScope
    fun provideService(retrofit: Retrofit): ProductService {
        return retrofit.create(ProductService::class.java)
    }

    @Provides
    @SprinkleAppScope
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    @Provides
    @SprinkleAppScope
    fun gson(): Gson {
        return GsonBuilder().create()
    }

}