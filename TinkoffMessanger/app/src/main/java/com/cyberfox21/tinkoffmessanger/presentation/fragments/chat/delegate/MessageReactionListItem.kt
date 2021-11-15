package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate

import com.cyberfox21.tinkoffmessanger.presentation.DelegateItem

class MessageReactionListItem(
    val name: String,
    val reaction: String,
    val count: Int,
    val isSelected: Boolean
) : DelegateItem {
    override fun id() = name
    override fun content() = count
}