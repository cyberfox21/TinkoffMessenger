package com.cyberfox21.tinkoffmessanger.util

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.ChannelDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.TopicDelegateItem

private fun mapToChannelDelegateItem(item: Channel): DelegateItem {
    return ChannelDelegateItem(
        item.hashCode(),
        item
    )
}

private fun mapToTopicDelegateItem(item: Topic): DelegateItem {
    return TopicDelegateItem(
        item.hashCode(),
        item
    )
}

fun List<Channel>.toDelegateChannelItemsList(pos: Int): MutableList<DelegateItem> {
    val delegateItemList = mutableListOf<DelegateItem>()
    var count = 0
    this.forEach { channel ->
        delegateItemList.add(mapToChannelDelegateItem(channel))
        if (pos == count) {
            channel.listOfTopics.forEach {
                delegateItemList.add(mapToTopicDelegateItem(it))
            }
        }
        count++
    }
    return delegateItemList
}

