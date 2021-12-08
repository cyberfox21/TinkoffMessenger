package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import vivid.money.elmslie.core.ElmStoreCompat

class ChannelsStoreFactory(private val actor: ChannelsActor) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = ChannelsState(),
            reducer = ChannelsReducer(),
            actor = actor
        )
    }

    fun provide() = store

}
