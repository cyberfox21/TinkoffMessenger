package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.ChannelDelegateItem

data class ChannelsState(
    val delegateItems: List<DelegateItem> = listOf(),
    val delegateChannels: List<ChannelDelegateItem> = listOf(),
    val isEmptyState: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
