package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

class LoadingDelegateItem : ChatDelegateItem(id = LOADING_ITEM_ID) {
    override fun content() = "loading"

    override fun id() = id

    companion object {
        const val LOADING_ITEM_ID = -3
    }
}