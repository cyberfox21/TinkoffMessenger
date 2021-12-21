package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

import android.text.SpannableString
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

open class MessageDelegateItem(
    override val id: Int,
    open val senderId: Int,
    open val text: SpannableString,
    open val time: String,
    open val reactions: List<MessageReactionListItem>
) : ChatDelegateItem(id), DelegateItem {

    override fun id(): Any = id

    override fun content(): Any = text
}