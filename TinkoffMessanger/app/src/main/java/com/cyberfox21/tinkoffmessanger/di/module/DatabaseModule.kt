package com.cyberfox21.tinkoffmessanger.di.module

import android.content.Context
import androidx.room.Room
import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase
import com.cyberfox21.tinkoffmessanger.data.database.dao.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "database").build()

    @Provides
    @Singleton
    fun provideMessagesDao(appDatabase: AppDatabase): MessagesDao = appDatabase.messagesDao()

    @Provides
    @Singleton
    fun provideAllChannelsDao(appDatabase: AppDatabase): AllChannelsDao =
        appDatabase.allChannelsDao()

    @Provides
    @Singleton
    fun provideSubscribedChannelsDao(appDatabase: AppDatabase): SubscribedChannelsDao =
        appDatabase.subscribedChannelsDao()

    @Provides
    @Singleton
    fun provideReactionListDao(appDatabase: AppDatabase): ReactionListDao =
        appDatabase.reactionListDao()

    @Provides
    @Singleton
    fun provideTopicsDao(appDatabase: AppDatabase): TopicsDao = appDatabase.topicsDao()

    @Provides
    @Singleton
    fun provideUsersDao(appDatabase: AppDatabase): UsersDao = appDatabase.usersDao()

}
