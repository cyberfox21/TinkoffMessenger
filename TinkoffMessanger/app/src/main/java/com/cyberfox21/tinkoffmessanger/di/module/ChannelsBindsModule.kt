package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.data.repository.ChannelsRepositoryImpl
import com.cyberfox21.tinkoffmessanger.data.repository.TopicsRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.repository.ChannelsRepository
import com.cyberfox21.tinkoffmessanger.domain.repository.TopicsRepository
import dagger.Binds
import dagger.Module

@Module
interface ChannelsBindsModule {
    @Binds
    fun bindChannelsRepository(channelsRepositoryImpl: ChannelsRepositoryImpl): ChannelsRepository

    @Binds
    fun bindTopicsRepository(topicsRepositoryImpl: TopicsRepositoryImpl): TopicsRepository
}