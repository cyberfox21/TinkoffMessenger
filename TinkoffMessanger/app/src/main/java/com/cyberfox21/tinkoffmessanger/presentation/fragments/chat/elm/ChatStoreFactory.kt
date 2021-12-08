package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import vivid.money.elmslie.core.ElmStoreCompat

class ChatStoreFactory(private val actor: ChatActor) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = ChatState(),
            reducer = ChatReducer(),
            actor = actor
        )
    }

    fun provide() = store

}
