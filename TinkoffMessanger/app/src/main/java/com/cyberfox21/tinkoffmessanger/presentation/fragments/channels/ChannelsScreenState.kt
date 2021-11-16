package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

sealed class ChannelsScreenState {

    class Result(val items: MutableList<DelegateItem>) : ChannelsScreenState()

    object Loading : ChannelsScreenState()

    class Error(val error: Throwable) : ChannelsScreenState()
}
