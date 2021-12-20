package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

class DateDelegateItem(private val text: String) : ChatDelegateItem(id = DATE_ITEM_ID) {
    override fun content(): Any = text
    override fun id(): Any = text

    companion object {
        const val DATE_ITEM_ID = -2
    }
}
