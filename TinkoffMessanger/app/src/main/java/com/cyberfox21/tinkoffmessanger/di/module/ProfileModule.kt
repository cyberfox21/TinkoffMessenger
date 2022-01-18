package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.domain.usecase.GetMyUserUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetUserPresenceUseCase
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm.*
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.core.ElmStoreCompat

@Module
class ProfileModule {

    @Provides
    fun provideProfileStore(actor: ProfileActor)
            : ElmStoreCompat<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand> =
        ElmStoreCompat(initialState = ProfileState(), reducer = ProfileReducer(), actor = actor)

    @Provides
    fun provideProfileActor(
        getMyUserUseCase: GetMyUserUseCase,
        getUserPresenceUseCase: GetUserPresenceUseCase
    ): ProfileActor =
        ProfileActor(getMyUserUseCase, getUserPresenceUseCase)

}