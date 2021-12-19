package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

class DateDelegateItem(private val text: String) : ChatDelegateItem() {
    override fun content(): Any = text
    override fun id(): Any = text
}
