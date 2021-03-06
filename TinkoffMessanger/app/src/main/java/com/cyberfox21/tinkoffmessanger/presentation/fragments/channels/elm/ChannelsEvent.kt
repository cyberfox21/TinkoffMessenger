package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.enums.Category

sealed class ChannelsEvent {

    sealed class Ui : ChannelsEvent() {

        data class GetChannelsList(val searchQuery: String, val category: Category) : Ui()

        data class UpdateTopics(
            val channelId: Int,
            val channelName: String,
            val isSelected: Boolean
        ) : Ui()
    }

    sealed class Internal : ChannelsEvent() {

        data class ChannelsLoaded(val channels: List<Channel>) : Internal()

        object ChannelsLoadedEmpty : Internal()

        data class ChannelsLoadError(val error: Throwable) : Internal()

        data class TopicsLoaded(
            val topics: List<Topic>,
            val channelId: Int,
            val channelName: String,
            val isSelected: Boolean
        ) : Internal()

        object TopicsLoadedEmpty : Internal()

        data class TopicsLoadError(val error: Throwable) : Internal()
    }
}
