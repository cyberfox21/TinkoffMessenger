package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.domain.usecase.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.ChannelsActor
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm.ChatActor
import com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm.PeopleActor
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.ProfileActor
import dagger.Module
import dagger.Provides

@Module
class ActorModule {

    @Provides
    fun provideProfileActor(
        getMyUserUseCase: GetMyUserUseCase,
        getUserPresenceUseCase: GetUserPresenceUseCase
    ): ProfileActor =
        ProfileActor(getMyUserUseCase, getUserPresenceUseCase)

    @Provides
    fun providePeopleActor(getUsersListUseCase: GetUsersListUseCase): PeopleActor =
        PeopleActor(getUsersListUseCase)

    @Provides
    fun provideChannelsActor(
        searchChannelsUseCase: SearchChannelsUseCase,
        getTopicsUseCase: GetTopicsUseCase
    ): ChannelsActor = ChannelsActor(searchChannelsUseCase, getTopicsUseCase)

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
