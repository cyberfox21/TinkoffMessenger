package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

import android.text.SpannableString

class MyMessageDelegateItem(
    val id: Int,
    val text: SpannableString,
    val time: String,
    val reactions: List<MessageReactionListItem>
) : ChatDelegateItem() {

    override fun id(): Int = id

    override fun content() = reactions
}
