package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.Category

sealed class ChannelsCommand {
    data class GetTopicsList(val channelId: Int) : ChannelsCommand()
    data class GetChannelsList(val searchQuery: String, val category: Category) : ChannelsCommand()
}