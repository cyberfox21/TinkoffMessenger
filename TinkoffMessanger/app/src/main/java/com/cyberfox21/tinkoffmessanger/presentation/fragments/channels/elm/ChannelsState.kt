package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

data class ChannelsState(
    val channels: List<DelegateItem> = listOf(),
    val isEmptyState: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
