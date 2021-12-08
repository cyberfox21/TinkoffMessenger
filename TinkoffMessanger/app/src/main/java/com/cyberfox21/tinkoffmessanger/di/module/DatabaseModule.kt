package com.cyberfox21.tinkoffmessanger.di.module

import android.content.Context
import androidx.room.Room
import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase
import com.cyberfox21.tinkoffmessanger.data.database.dao.*
import com.cyberfox21.tinkoffmessanger.di.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    @ApplicationScope
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "database").build()

    @Provides
    @ApplicationScope
    fun provideMessagesDao(appDatabase: AppDatabase): MessagesDao = appDatabase.messagesDao()

    @Provides
    @ApplicationScope
    fun provideAllChannelsDao(appDatabase: AppDatabase): AllChannelsDao =
        appDatabase.allChannelsDao()

    @Provides
    @ApplicationScope
    fun provideSubscribedChannelsDao(appDatabase: AppDatabase): SubscribedChannelsDao =
        appDatabase.subscribedChannelsDao()

    @Provides
    @ApplicationScope
    fun provideReactionListDao(appDatabase: AppDatabase): ReactionListDao =
        appDatabase.reactionListDao()

    @Provides
    @ApplicationScope
    fun provideTopicsDao(appDatabase: AppDatabase): TopicsDao =
        appDatabase.topicsDao()

    @Provides
    @ApplicationScope
    fun provideUsersDao(appDatabase: AppDatabase): UsersDao = appDatabase.usersDao()

}
