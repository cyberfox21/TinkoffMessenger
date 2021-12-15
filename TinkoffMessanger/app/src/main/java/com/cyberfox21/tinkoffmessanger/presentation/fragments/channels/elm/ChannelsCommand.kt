package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.Category

sealed class ChannelsCommand {
    data class GetTopicsList(val channelId: Int, val channelName: String, val isSelected: Boolean) :
        ChannelsCommand()

    data class GetChannelsList(val searchQuery: String, val category: Category) : ChannelsCommand()
}
