package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.*
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.core.ElmStoreCompat

@Module
class StoreModule {

    @Provides
    fun provideChannelsStore(actor: ChannelsActor)
            : ElmStoreCompat<ChannelsEvent, ChannelsState, ChannelsEffect, ChannelsCommand> =
        ElmStoreCompat(initialState = ChannelsState(), reducer = ChannelsReducer(), actor = actor)

    @Provides
    fun providePeopleStore(actor: PeopleActor)
            : ElmStoreCompat<PeopleEvent, PeopleState, PeopleEffect, PeopleCommand> =
        ElmStoreCompat(initialState = PeopleState(), reducer = PeopleReducer(), actor = actor)

    @Provides
    fun provideProfileStore(actor: ProfileActor)
            : ElmStoreCompat<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand> =
        ElmStoreCompat(initialState = ProfileState(), reducer = ProfileReducer(), actor = actor)

    @Provides
    fun provideChatStore(actor: ChatActor)
            : ElmStoreCompat<ChatEvent, ChatState, ChatEffect, ChatCommand> =
        ElmStoreCompat(initialState = ChatState(), reducer = ChatReducer(), actor = actor)
}