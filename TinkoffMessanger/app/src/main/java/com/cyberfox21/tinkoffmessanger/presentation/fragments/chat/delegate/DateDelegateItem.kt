package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate

import com.cyberfox21.tinkoffmessanger.presentation.DelegateItem

class DateDelegateItem(private val text: String) : DelegateItem {
    override fun content(): Any = text
    override fun id(): Any = text
}