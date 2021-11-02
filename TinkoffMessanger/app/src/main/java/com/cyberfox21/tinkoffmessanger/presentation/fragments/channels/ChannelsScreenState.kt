package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel

sealed class ChannelsScreenState {
    class Result(val items: List<Channel>) : ChannelsScreenState()

    object Loading : ChannelsScreenState()

    class Error(val error: Throwable) : ChannelsScreenState()
}
