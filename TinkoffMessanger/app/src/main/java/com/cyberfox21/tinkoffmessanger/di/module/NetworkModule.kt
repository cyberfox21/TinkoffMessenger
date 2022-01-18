package com.cyberfox21.tinkoffmessanger.di.module

import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .addHeader(
                        AUTHORIZATION,
                        Credentials.basic("shkolniktatyana21@gmail.com", API_KEY)
                    ).build()
            chain.proceed(request)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val API_KEY = "SLZzHVbnLRJXOvJUVljsKaYLCHLTouZK"
        const val BASE_URL = "https://tinkoff-android-fall21.zulipchat.com/api/v1/"
    }

}
