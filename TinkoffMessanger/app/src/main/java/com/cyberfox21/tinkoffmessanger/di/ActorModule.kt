package com.cyberfox21.tinkoffmessanger.di

import com.cyberfox21.tinkoffmessanger.domain.usecase.GetMyUserUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetTopicsUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetUsersListUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.SearchChannelsUseCase
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.ChannelsActor
import com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm.PeopleActor
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.ProfileActor
import dagger.Module
import dagger.Provides

@Module
class ActorModule {

    @Provides
    fun provideProfileActor(getMyUserUseCase: GetMyUserUseCase): ProfileActor =
        ProfileActor(getMyUserUseCase)

    @Provides
    fun providePeopleActor(getUsersListUseCase: GetUsersListUseCase): PeopleActor =
        PeopleActor(getUsersListUseCase)

    @Provides
    fun provideChannelsActor(
        searchChannelsUseCase: SearchChannelsUseCase,
        getTopicsUseCase: GetTopicsUseCase
    ): ChannelsActor = ChannelsActor(searchChannelsUseCase, getTopicsUseCase)

//    @Provides
//    fun provideChatActor(getMyUserUseCase : GetMyUserUseCase) : ChatActor = ChatActor(getMyUserUseCase)

}