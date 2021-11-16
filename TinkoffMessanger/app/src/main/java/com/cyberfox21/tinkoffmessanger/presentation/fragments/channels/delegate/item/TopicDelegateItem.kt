package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

data class TopicDelegateItem(
    val name: String,
    val msgCount: Int
) : DelegateItem {
    override fun content() = msgCount
    override fun id() = name
}
