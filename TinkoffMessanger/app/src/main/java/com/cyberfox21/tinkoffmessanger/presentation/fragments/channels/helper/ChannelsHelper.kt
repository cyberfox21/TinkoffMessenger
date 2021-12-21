package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.helper

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.presentation.common.mapToChannelDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.common.mapToTopicDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.ChannelDelegateItem

object ChannelsHelper {

    fun getChannelDelegateItemsList(listOfChannels: List<Channel>): List<ChannelDelegateItem> =
        listOfChannels.map { it.mapToChannelDelegateItem(selected = false) }.toMutableList()


    fun getDelegateItemsList(
        delegateChannels: List<ChannelDelegateItem>,
        topics: List<Topic>,
        channelId: Int,
        isSelected: Boolean
    ): List<DelegateItem> {
        if (isSelected) {
            return delegateChannels
        }
        val delegateItemsList = (delegateChannels as List<DelegateItem>).toMutableList()
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