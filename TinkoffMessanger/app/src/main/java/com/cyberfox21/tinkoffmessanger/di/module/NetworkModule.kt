package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.data.network.api.*
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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
    @Named("MESSENGER")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    @Named("ChannelsApi")
    fun provideChannelsApi(@Named("MESSENGER") retrofit: Retrofit): ChannelsApi =
        retrofit.create(ChannelsApi::class.java)

    @Provides
    @Singleton
    @Named("MessagesApi")
    fun provideMessagesApi(@Named("MESSENGER") retrofit: Retrofit): MessagesApi =
        retrofit.create(MessagesApi::class.java)

    @Provides
    @Singleton
    @Named("ReactionsApi")
    fun provideReactionsApi(@Named("MESSENGER") retrofit: Retrofit): ReactionsApi =
        retrofit.create(ReactionsApi::class.java)

    @Provides
    @Singleton
    @Named("TopicsApi")
    fun provideTopicsApi(@Named("MESSENGER") retrofit: Retrofit): TopicsApi =
        retrofit.create(TopicsApi::class.java)

    @Provides
    @Singleton
    @Named("UsersApi")
    fun provideUsersApi(@Named("MESSENGER") retrofit: Retrofit): UsersApi =
        retrofit.create(UsersApi::class.java)

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val API_KEY = "SLZzHVbnLRJXOvJUVljsKaYLCHLTouZK"
        const val BASE_URL = "https://tinkoff-android-fall21.zulipchat.com/api/v1/"
    }

}
