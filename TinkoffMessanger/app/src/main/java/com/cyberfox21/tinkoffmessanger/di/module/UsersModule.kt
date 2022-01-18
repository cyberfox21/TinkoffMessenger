package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase
import com.cyberfox21.tinkoffmessanger.data.database.dao.UsersDao
import com.cyberfox21.tinkoffmessanger.data.network.api.UsersApi
import com.cyberfox21.tinkoffmessanger.di.qualifier.UsersApiQualifier
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class UsersModule {

    @Provides
    @Singleton
    fun provideUsersDao(appDatabase: AppDatabase): UsersDao = appDatabase.usersDao()

    @Provides
    @Singleton
    @UsersApiQualifier
    fun provideUsersApi(retrofit: Retrofit): UsersApi =
        retrofit.create(UsersApi::class.java)

}