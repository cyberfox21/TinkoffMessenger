package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel

class ChannelDelegateItem(
    val id: Int,
    private val channel: Channel
) : DelegateItem {
    override fun content(): Any = channel
    override fun id(): Int = channel.hashCode()
}
