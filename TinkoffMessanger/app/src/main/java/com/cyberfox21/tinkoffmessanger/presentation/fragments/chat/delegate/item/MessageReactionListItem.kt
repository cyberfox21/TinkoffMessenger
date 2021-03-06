package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

data class MessageReactionListItem(
    val name: String,
    val code: String,
    val type: String,
    val reaction: String,
    val count: Int,
    val senderId: Int,
    val isSelected: Boolean
) : DelegateItem {
    override fun id() = name
    override fun content() = count
}
