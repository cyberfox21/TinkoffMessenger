package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate

import com.cyberfox21.tinkoffmessanger.presentation.DelegateItem

class MyMessageDelegateItem(
    private val id: Int,
    val text: String,
    val time: String,
    val reactions: List<MessageReactionListItem>
) : DelegateItem {

    override fun id(): Int = id

    override fun content() = reactions
}