package com.cyberfox21.tinkoffmessanger.di

import android.content.Context
import com.cyberfox21.tinkoffmessanger.di.module.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.ChannelsCommand
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.ChannelsEffect
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.ChannelsEvent
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm.ChannelsState
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm.ChatCommand
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm.ChatEffect
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm.ChatEvent
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm.ChatState
import com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm.PeopleCommand
import com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm.PeopleEffect
import com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm.PeopleEvent
import com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm.PeopleState
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.ProfileCommand
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.ProfileEffect
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.ProfileEvent
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.ProfileState
import dagger.BindsInstance
import dagger.Component
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Named
import javax.inject.Singleton

@Singleton

@Component(
    modules = [DatabaseModule::class, NetworkModule::class,
        ActorModule::class, RepositoryModule::class, StoreModule::class]
)
interface ApplicationComponent {

    @Named("channelsStore")
    val channelsStore
            : ElmStoreCompat<ChannelsEvent, ChannelsState, ChannelsEffect, ChannelsCommand>

    @Named("peopleStore")
    val peopleStore
            : ElmStoreCompat<PeopleEvent, PeopleState, PeopleEffect, PeopleCommand>

    @Named("profileStore")
    val profileStore
            : ElmStoreCompat<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>

    @Named("chatStore")
    val chatStore
            : ElmStoreCompat<ChatEvent, ChatState, ChatEffect, ChatCommand>

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(@BindsInstance context: Context): ApplicationComponent

    }

}
