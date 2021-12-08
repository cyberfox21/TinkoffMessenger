package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

class DateDelegateItem(private val text: String) : DelegateItem {
    override fun content(): Any = text
    override fun id(): Any = text
}
