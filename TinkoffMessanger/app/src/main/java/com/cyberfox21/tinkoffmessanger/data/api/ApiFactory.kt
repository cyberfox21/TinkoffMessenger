package com.cyberfox21.tinkoffmessanger.data.api

import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {

    private const val BASE_URL = "https://tinkoff-android-fall21.zulipchat.com/api/v1/"

    private const val API_KEY = "SLZzHVbnLRJXOvJUVljsKaYLCHLTouZK"

    private var MY_TOKEN = Credentials.basic("shkolniktatyana21@gmail.com", API_KEY)

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .addHeader("Authorization", MY_TOKEN).build()
            chain.proceed(request)
        }.build())
        .baseUrl(BASE_URL)
        .build()

    val api: Api = retrofit.create(Api::class.java)

}
