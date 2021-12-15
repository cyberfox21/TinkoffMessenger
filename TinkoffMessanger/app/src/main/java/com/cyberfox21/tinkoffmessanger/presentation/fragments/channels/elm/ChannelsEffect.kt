package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem

sealed class ChannelsEffect {
    object EmptyChannels : ChannelsEffect()
    data class ChannelsLoadError(val error: Throwable) : ChannelsEffect()

    object Loading : ChannelsEffect()

    object EmptyTopics : ChannelsEffect()
    data class TopicsLoadError(val error: Throwable) : ChannelsEffect()

    data class RefreshTopics(val items: List<DelegateItem>) : ChannelsEffect()
}
