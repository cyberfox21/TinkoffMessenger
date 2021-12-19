package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

class MessageReactionListItem(
    val name: String,
    val code: String,
    val type: String,
    val reaction: String,
    val count: Int,
    val isSelected: Boolean
) : ChatDelegateItem() {
    override fun id() = name
    override fun content() = count
}
