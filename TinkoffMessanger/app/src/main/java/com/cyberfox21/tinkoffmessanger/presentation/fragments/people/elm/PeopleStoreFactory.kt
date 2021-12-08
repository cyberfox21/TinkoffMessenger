package com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm

import vivid.money.elmslie.core.ElmStoreCompat

class PeopleStoreFactory(private val actor: PeopleActor) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = PeopleState(),
            reducer = PeopleReducer(),
            actor = actor
        )
    }

    fun provide() = store

}
