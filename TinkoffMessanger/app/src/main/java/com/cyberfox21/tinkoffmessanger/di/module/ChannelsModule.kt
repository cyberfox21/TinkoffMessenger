package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase
import com.cyberfox21.tinkoffmessanger.data.database.dao.AllChannelsDao
import com.cyberfox21.tinkoffmessanger.data.database.dao.SubscribedChannelsDao
import com.cyberfox21.tinkoffmessanger.data.database.dao.TopicsDao
import com.cyberfox21.tinkoffmessanger.data.network.api.ChannelsApi
import com.cyberfox21.tinkoffmessanger.data.network.api.TopicsApi
import com.cyberfox21.tinkoffmessanger.di.qualifier.ChannelsApiQualifier
import com.cyberfox21.tinkoffmessanger.di.qualifier.TopicsApiQualifier
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetTopicsUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.SearchChannelsUseCase
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Singleton

@Module
class ChannelsModule {

    @Provides
    @Singleton
    @ChannelsApiQualifier
    fun provideChannelsApi(retrofit: Retrofit): ChannelsApi =
        retrofit.create(ChannelsApi::class.java)

    @Provides
    @Singleton
    @TopicsApiQualifier
    fun provideTopicsApi(retrofit: Retrofit): TopicsApi =
        retrofit.create(TopicsApi::class.java)

    @Provides
    fun provideChannelsStore(actor: ChannelsActor)
            : ElmStoreCompat<ChannelsEvent, ChannelsState, ChannelsEffect, ChannelsCommand> =
        ElmStoreCompat(initialState = ChannelsState(), reducer = ChannelsReducer(), actor = actor)

    @Provides
    fun provideChannelsActor(
        searchChannelsUseCase: SearchChannelsUseCase,
        getTopicsUseCase: GetTopicsUseCase
    ): ChannelsActor = ChannelsActor(searchChannelsUseCase, getTopicsUseCase)

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
    fun provideTopicsDao(appDatabase: AppDatabase): TopicsDao = appDatabase.topicsDao()

}