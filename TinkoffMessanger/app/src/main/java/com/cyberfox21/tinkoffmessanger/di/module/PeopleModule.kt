package com.cyberfox21.tinkoffmessanger.di.module

import com.cyberfox21.tinkoffmessanger.domain.usecase.GetUsersListUseCase
import com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm.*
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.core.ElmStoreCompat

@Module
class PeopleModule {

    @Provides
    fun providePeopleStore(actor: PeopleActor)
            : ElmStoreCompat<PeopleEvent, PeopleState, PeopleEffect, PeopleCommand> =
        ElmStoreCompat(initialState = PeopleState(), reducer = PeopleReducer(), actor = actor)

    @Provides
    fun providePeopleActor(getUsersListUseCase: GetUsersListUseCase): PeopleActor =
        PeopleActor(getUsersListUseCase)
}