package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

class AlienMessageDelegateItem(
    private val id: Int,
    val text: String,
    val time: String,
    val senderName: String,
    val senderAvatarUrl: String,
    val reactions: List<MessageReactionListItem>
) : DelegateItem {

    override fun id(): Int = id

    override fun content() = reactions

}