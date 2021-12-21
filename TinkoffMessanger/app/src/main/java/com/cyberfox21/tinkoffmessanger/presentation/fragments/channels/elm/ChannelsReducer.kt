package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.helper.ChannelsHelper
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ChannelsReducer :
    DslReducer<ChannelsEvent, ChannelsState, ChannelsEffect, ChannelsCommand>() {

    override fun Result.reduce(event: ChannelsEvent): Any {
        return when (event) {
            is ChannelsEvent.Internal.ChannelsLoaded -> {
                state {
                    copy(
                        delegateItems = ChannelsHelper.getChannelDelegateItemsList(event.channels),
                        delegateChannels = ChannelsHelper.getChannelDelegateItemsList(event.channels),
                        channelStatus = ResourceStatus.SUCCESS
                    )
                }
            }
            ChannelsEvent.Internal.ChannelsLoadedEmpty -> {
                if (state.channelStatus != ResourceStatus.SUCCESS) {
                    state { copy(channelStatus = ResourceStatus.EMPTY) }
                } else {
                    state { copy(channelStatus = ResourceStatus.SUCCESS) }
                }
            }
            is ChannelsEvent.Internal.ChannelsLoadError -> {
                if (state.delegateItems.isEmpty()) {
                    state {
                        copy(
                            channelStatus = ResourceStatus.ERROR,
                            error = event.error
                        )
                    }
                } else {
                    state { copy(channelStatus = ResourceStatus.SUCCESS) }
                }
            }
            is ChannelsEvent.Internal.TopicsLoaded -> {
                state {
                    copy(
                        selectedChannelName = event.channelName,
                        selectedChannelId = event.channelId,
                        delegateItems = ChannelsHelper.getDelegateItemsList(
                            state.delegateChannels,
                            event.topics,
                            event.channelId,
                            event.isSelected
                        ),
                        topicStatus = ResourceStatus.SUCCESS
                    )
                }
                effects {
                    ChannelsEffect.RefreshTopics(
                        ChannelsHelper.getDelegateItemsList(
                            state.delegateChannels,
                            event.topics,
                            event.channelId,
                            event.isSelected
                        )
                    )
                }
            }
            ChannelsEvent.Internal.TopicsLoadedEmpty -> {
                state {
                    copy(
                        delegateItems = state.delegateChannels,
                        topicStatus = ResourceStatus.EMPTY
                    )
                }
            }
            is ChannelsEvent.Internal.TopicsLoadError -> {
                state { copy(topicStatus = ResourceStatus.ERROR) }
            }

            is ChannelsEvent.Ui.GetChannelsList -> {
                state { copy(channelStatus = ResourceStatus.LOADING) }
                commands { +ChannelsCommand.GetChannelsList(event.searchQuery, event.category) }

            }
            is ChannelsEvent.Ui.UpdateTopics -> {
                if (!event.isSelected) {
                    state { copy(topicStatus = ResourceStatus.LOADING) }
                    commands {
                        +ChannelsCommand.GetTopicsList(
                            event.channelId,
                            event.channelName,
                            event.isSelected
                        )
                    }
                } else {
                    state {
                        copy(
                            topicStatus = ResourceStatus.SUCCESS,
                            delegateItems = state.delegateChannels
                        )
                    }
                }
            }
        }
    }


}

