package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item

import android.os.Parcelable
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import kotlinx.parcelize.Parcelize

@Parcelize
open class ChatDelegateItem : DelegateItem, Parcelable {
    override fun content() = Any()

    override fun id() = Any()
}