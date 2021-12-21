package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

import android.text.SpannableString

class MyMessageDelegateItem(
    override val id: Int,
    myId: Int,
    override val text: SpannableString,
    override val time: String,
    override val reactions: List<MessageReactionListItem>
) : MessageDelegateItem(id = id, text = text, time = time, reactions = reactions, senderId = myId) {

    override fun id(): Int = id

    override fun content() = this
}
