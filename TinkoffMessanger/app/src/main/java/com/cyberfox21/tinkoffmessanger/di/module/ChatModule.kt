package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase
import com.cyberfox21.tinkoffmessanger.data.database.dao.MessagesDao
import com.cyberfox21.tinkoffmessanger.data.database.dao.ReactionListDao
import com.cyberfox21.tinkoffmessanger.data.network.api.MessagesApi
import com.cyberfox21.tinkoffmessanger.data.network.api.ReactionsApi
import com.cyberfox21.tinkoffmessanger.di.qualifier.MessagesApiQualifier
import com.cyberfox21.tinkoffmessanger.di.qualifier.ReactionsApiQualifier
import com.cyberfox21.tinkoffmessanger.domain.usecase.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Singleton

@Module
class ChatModule {

    @Provides
    @Singleton
    @MessagesApiQualifier
    fun provideMessagesApi(retrofit: Retrofit): MessagesApi =
        retrofit.create(MessagesApi::class.java)

    @Provides
    @Singleton
    @ReactionsApiQualifier
    fun provideReactionsApi(retrofit: Retrofit): ReactionsApi =
        retrofit.create(ReactionsApi::class.java)

    @Provides
    @Singleton
    fun provideMessagesDao(appDatabase: AppDatabase): MessagesDao = appDatabase.messagesDao()

    @Provides
    @Singleton
    fun provideReactionListDao(appDatabase: AppDatabase): ReactionListDao =
        appDatabase.reactionListDao()

    @Provides
    fun provideChatStore(actor: ChatActor)
            : ElmStoreCompat<ChatEvent, ChatState, ChatEffect, ChatCommand> =
        ElmStoreCompat(initialState = ChatState(), reducer = ChatReducer(), actor = actor)

    @Provides
    fun provideChatActor(
        getMessageListUseCase: GetMessageListUseCase,
        getMessageUseCase: GetMessageUseCase,
        addMessageUseCase: AddMessageUseCase,
        editMessageUseCase: EditMessageUseCase,
        changeTopicUseCase: ChangeTopicUseCase,
        deleteMessageUseCase: DeleteMessageUseCase,
        getReactionListUseCase: GetReactionListUseCase,
        addReactionUseCase: AddReactionUseCase,
        deleteReactionUseCase: DeleteReactionUseCase,
        getMyUserUseCase: GetMyUserUseCase
    ): ChatActor = ChatActor(
        getMessageListUseCase,
        getMessageUseCase,
        addMessageUseCase,
        editMessageUseCase,
        changeTopicUseCase,
        deleteMessageUseCase,
        getReactionListUseCase,
        addReactionUseCase,
        deleteReactionUseCase,
        getMyUserUseCase
    )
}