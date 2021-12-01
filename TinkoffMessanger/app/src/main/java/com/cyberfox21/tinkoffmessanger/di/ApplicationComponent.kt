package com.cyberfox21.tinkoffmessanger.di

import android.content.Context
import com.cyberfox21.tinkoffmessanger.di.module.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.ListChannelsFragment
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.ChatFragment
import com.cyberfox21.tinkoffmessanger.presentation.fragments.people.PeopleFragment
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.ProfileFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope

@Component(
    modules = [DatabaseModule::class, NetworkModule::class,
        ActorModule::class, RepositoryModule::class, UseCaseModule::class]
)
interface ApplicationComponent {

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent

    }

    fun injectProfileFragment(profileFragment: ProfileFragment)
    fun injectPeopleFragment(peopleFragment: PeopleFragment)
    fun injectChannelsFragment(listChannelsFragment: ListChannelsFragment)
    fun injectChatFragment(chatFragment: ChatFragment)

}