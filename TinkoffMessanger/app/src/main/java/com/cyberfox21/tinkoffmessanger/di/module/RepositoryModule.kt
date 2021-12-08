package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.data.repository.*
import com.cyberfox21.tinkoffmessanger.domain.repository.*
import dagger.Binds
import dagger.Module
import kotlinx.serialization.ExperimentalSerializationApi

@Module
interface RepositoryModule {

    @Binds
    fun bindChannelsRepository(channelsRepositoryImpl: ChannelsRepositoryImpl): ChannelsRepository

    @Binds
    @ExperimentalSerializationApi
    fun bindMessagesRepository(messagesRepositoryImpl: MessagesRepositoryImpl): MessagesRepository

    @Binds
    fun bindReactionsRepository(reactionsRepositoryImpl: ReactionsRepositoryImpl): ReactionsRepository

    @Binds
    fun bindTopicsRepository(topicsRepositoryImpl: TopicsRepositoryImpl): TopicsRepository

    @Binds
    fun bindUsersRepository(usersRepositoryImpl: UsersRepositoryImpl): UsersRepository

}
