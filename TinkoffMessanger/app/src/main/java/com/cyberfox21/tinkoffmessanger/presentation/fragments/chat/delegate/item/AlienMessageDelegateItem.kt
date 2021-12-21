package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

import android.text.SpannableString

class AlienMessageDelegateItem(
    override val id: Int,
    override val text: SpannableString,
    override val time: String,
    override val senderId: Int,
    val senderName: String,
    val senderAvatarUrl: String,
    override val reactions: List<MessageReactionListItem>
) : MessageDelegateItem(
    id = id,
    text = text,
    time = time,
    reactions = reactions,
    senderId = senderId
) {

    override fun id(): Int = id

    override fun content() = reactions

}
