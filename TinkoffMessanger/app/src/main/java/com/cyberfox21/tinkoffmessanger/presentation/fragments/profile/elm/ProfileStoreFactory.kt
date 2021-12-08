package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

import vivid.money.elmslie.core.ElmStoreCompat

class ProfileStoreFactory(private val actor: ProfileActor) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = ProfileState(),
            reducer = ProfileReducer(),
            actor = actor
        )
    }

    fun provide() = store

}
