package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

sealed class ChannelsEffect {
    object EmptyChannels : ChannelsEffect()
    data class ChannelsLoadError(val error: Throwable) : ChannelsEffect()

    object EmptyTopics : ChannelsEffect()
    data class TopicsLoadError(val error: Throwable) : ChannelsEffect()
}