package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.data.repository.MessagesRepositoryImpl
import com.cyberfox21.tinkoffmessanger.data.repository.ReactionsRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionsRepository
import dagger.Binds
import dagger.Module
import kotlinx.serialization.ExperimentalSerializationApi

@Module
interface ChatBindsModule {

    @Binds
    @ExperimentalSerializationApi
    fun bindMessagesRepository(messagesRepositoryImpl: MessagesRepositoryImpl): MessagesRepository

    @Binds
    fun bindReactionsRepository(reactionsRepositoryImpl: ReactionsRepositoryImpl): ReactionsRepository

}