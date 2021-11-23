package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.ChannelDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.mapToChannelDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.mapToTopicDelegateItem
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ChannelsReducer :
    DslReducer<ChannelsEvent, ChannelsState, ChannelsEffect, ChannelsCommand>() {
    override fun Result.reduce(event: ChannelsEvent): Any {
        return when (event) {
            is ChannelsEvent.Internal.ChannelsLoaded -> {
                if (event.channels.isEmpty()) {
                    state {
                        copy(
                            channels = listOf(),
                            isEmptyState = true,
                            isLoading = false,
                            error = null
                        )
                    }
                    effects { ChannelsEffect.EmptyChannels }
                } else {
                    state {
                        copy(
                            channels = getChannelDelegateItemsList(event.channels),
                            isEmptyState = false,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
            is ChannelsEvent.Internal.ChannelsLoadError -> {
                state { copy(isLoading = false, error = event.error) }
                effects { ChannelsEffect.ChannelsLoadError(event.error) }
            }
            is ChannelsEvent.Internal.TopicsLoaded -> {
                if (event.topics.isEmpty()) {
                    state {
                        copy(
                            isLoading = false,
                            error = null
                        )
                    }
                    effects { ChannelsEffect.EmptyChannels }
                } else {
                    state {
                        copy(
                            channels = getDelegateItemsList(
                                state.channels,
                                event.topics,
                                event.channelId
                            ),
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
            is ChannelsEvent.Internal.TopicsLoadError -> {
                state { copy(isLoading = false, error = event.error) }
                effects { ChannelsEffect.TopicsLoadError(event.error) }
            }
            is ChannelsEvent.Ui.GetChannelsList -> {
                state { copy(isLoading = true) }
                commands { +ChannelsCommand.GetChannelsList(event.searchQuery, event.category) }
            }
            is ChannelsEvent.Ui.UpdateTopics -> {
                state { copy(isLoading = true) }
                commands { +ChannelsCommand.GetTopicsList(event.channelId) }
            }
        }
    }

    private fun getChannelDelegateItemsList(listOfChannels: List<Channel>): List<ChannelDelegateItem> {
        return listOfChannels.map {
            it.mapToChannelDelegateItem(selected = false)
        }.toMutableList()
    }

    private fun getDelegateItemsList(
        delegateItems: List<DelegateItem>,
        topics: List<Topic>,
        channelId: Int
    ): List<DelegateItem> {
        val delegateItemsList = delegateItems.toMutableList()
        val elementPosition =
            delegateItemsList.indexOfFirst { it is ChannelDelegateItem && it.id == channelId }
        val channel = delegateItemsList[elementPosition]
        delegateItemsList.removeAt(elementPosition)
        delegateItemsList.add(
            elementPosition,
            (channel as ChannelDelegateItem).copy(isSelected = true)
        )
        delegateItemsList.addAll(elementPosition + 1, topics.map {
            it.mapToTopicDelegateItem()
        })
        return delegateItemsList
    }

}