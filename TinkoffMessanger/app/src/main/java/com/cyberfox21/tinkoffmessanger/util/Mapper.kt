package com.cyberfox21.tinkoffmessanger.util

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.presentation.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.ChannelDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.TopicDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.AlienMessageDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.DateDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.MessageReactionListItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.MyMessageDelegateItem

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

fun List<Message>.toDelegateChatItemsList(userId: Int): List<DelegateItem> {
    val delegateItemList = mutableListOf<DelegateItem>()
    var count = 0
    this.indices.forEach { index ->
        val message = this[index]
        val reactions = message.reactions

        val currentUserReactions = reactions.filter { it.userId == userId }
        val listReactions = mutableListOf<MessageReactionListItem>()

        reactions.distinctBy { it.reaction }.forEach {
            listReactions.add(
                MessageReactionListItem(
                    name = it.name,
                    reaction = it.reaction,
                    count = reactions.count { msgEmoji -> msgEmoji.reaction == it.reaction },
                    isSelected = currentUserReactions.contains(it)
                )
            )
        }

        if (index == 0 || !DateFormatter.isTheSameDay(message.time, this[index - 1].time)) {
            delegateItemList.add(DateDelegateItem(DateFormatter.getDateForChatItem(message.time)))
        }

        val listMessage = when (message.senderId == userId) {
            true -> {
                MyMessageDelegateItem(
                    message.id,
                    message.message,
                    DateFormatter.getTimeForMessage(message.time),
                    listReactions
                )
            }
            false -> {
                AlienMessageDelegateItem(
                    message.id,
                    message.message,
                    DateFormatter.getTimeForMessage(message.time),
                    message.senderName,
                    message.senderAvatarUrl,
                    listReactions
                )
            }
        }
        delegateItemList.add(listMessage)
    }
    return delegateItemList
}

