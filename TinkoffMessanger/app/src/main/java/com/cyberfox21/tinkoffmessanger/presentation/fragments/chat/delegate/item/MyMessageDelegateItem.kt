package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

import android.text.SpannableString
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

class MyMessageDelegateItem(
    val id: Int,
    val text: SpannableString,
    val time: String,
    val reactions: List<MessageReactionListItem>
) : DelegateItem {

    override fun id(): Int = id

    override fun content() = reactions
}
