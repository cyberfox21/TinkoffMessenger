package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.domain.repository.*
import com.cyberfox21.tinkoffmessanger.domain.usecase.*
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun provideAddMessageUseCase(messagesRepositoryImpl: MessagesRepository): AddMessageUseCase =
        AddMessageUseCase(messagesRepositoryImpl)

    @Provides
    fun provideAddReactionUseCase(reactionsRepositoryImpl: ReactionsRepository): AddReactionUseCase =
        AddReactionUseCase(reactionsRepositoryImpl)

    @Provides
    fun provideDeleteReactionUseCase(reactionsRepositoryImpl: ReactionsRepository): DeleteReactionUseCase =
        DeleteReactionUseCase(reactionsRepositoryImpl)

    @Provides
    fun provideGetMessageListUseCase(messagesRepositoryImpl: MessagesRepository): GetMessageListUseCase =
        GetMessageListUseCase(messagesRepositoryImpl)

    @Provides
    fun provideGetMyUserUseCase(usersRepositoryImpl: UsersRepository): GetMyUserUseCase =
        GetMyUserUseCase(usersRepositoryImpl)

    @Provides
    fun provideGetReactionListUseCase(reactionsRepositoryImpl: ReactionsRepository): GetReactionListUseCase =
        GetReactionListUseCase(reactionsRepositoryImpl)

    @Provides
    fun provideTopicsUseCase(topicsRepositoryImpl: TopicsRepository): GetTopicsUseCase =
        GetTopicsUseCase(topicsRepositoryImpl)

    @Provides
    fun provideGetUsersListUseCase(usersRepositoryImpl: UsersRepository): GetUsersListUseCase =
        GetUsersListUseCase(usersRepositoryImpl)

    @Provides
    fun provideGetUserUseCase(usersRepositoryImpl: UsersRepository): GetUserUseCase =
        GetUserUseCase(usersRepositoryImpl)

    @Provides
    fun provideSearchChannelsUseCase(channelsRepositoryImpl: ChannelsRepository): SearchChannelsUseCase =
        SearchChannelsUseCase(channelsRepositoryImpl)

}