package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

import android.os.Parcelable
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import kotlinx.parcelize.Parcelize

@Parcelize
open class ChatDelegateItem(
    open val id: Int
) : DelegateItem, Parcelable {

    override fun id(): Any = id

    override fun content(): Any = "chatDelegateItem"

}