package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

data class ChannelDelegateItem(
    val id: Int,
    val name: String,
    var isSelected: Boolean
) : DelegateItem {
    override fun content(): Any = isSelected
    override fun id(): Int = id
}
