package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

import android.text.SpannableString

class AlienMessageDelegateItem(
    val id: Int,
    val text: SpannableString,
    val time: String,
    val senderId: Int,
    val senderName: String,
    val senderAvatarUrl: String,
    val reactions: List<MessageReactionListItem>
) : ChatDelegateItem() {

    override fun id(): Int = id

    override fun content() = reactions

}
