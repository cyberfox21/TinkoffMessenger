package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

data class ChannelDelegateItem(
    val id: Int,
    val name: String,
    val isSelected: Boolean
) : DelegateItem {
    override fun content(): Any = name
    override fun id(): Int = id
}
