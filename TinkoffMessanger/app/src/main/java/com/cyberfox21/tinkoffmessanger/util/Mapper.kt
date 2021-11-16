package com.cyberfox21.tinkoffmessanger.util

import com.cyberfox21.tinkoffmessanger.data.api.response.dto.ChannelDTO
import com.cyberfox21.tinkoffmessanger.data.api.response.dto.SubscribedChannelDTO
import com.cyberfox21.tinkoffmessanger.data.api.response.dto.TopicDTO
import com.cyberfox21.tinkoffmessanger.data.database.model.ChannelDBModel
import com.cyberfox21.tinkoffmessanger.data.database.model.MessageDBModel
import com.cyberfox21.tinkoffmessanger.data.database.model.TopicDBModel
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.ChannelDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.TopicDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.AlienMessageDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.DateDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.MessageReactionListItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.MyMessageDelegateItem

fun List<Message>.toDelegateChatItemsList(userId: Int): List<DelegateItem> {
    val delegateItemList = mutableListOf<DelegateItem>()
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

fun MessageDBModel.mapToMessage() = Message(
    id = id,
    message = message,
    time = time,
    senderId = senderId,
    senderName = senderName,
    senderAvatarUrl = senderAvatarUrl,
    isCurrentUser = isCurrentUser,
    reactions = reactions
)

fun SubscribedChannelDTO.mapToChannel() = Channel(
    id = streamId,
    name = name,
)

fun ChannelDTO.mapToChannel() = Channel(
    id = streamId,
    name = name,
)

fun Channel.mapToChannelDelegateItem(selected: Boolean) = ChannelDelegateItem(
    id = id,
    name = name,
    isSelected = selected
)

fun Channel.mapToChannelDBModel(subscribed: Boolean) = ChannelDBModel(
    id = id,
    name = name,
    subscribed = subscribed
)

fun ChannelDBModel.mapToChannel() = Channel(
    id = id,
    name = name
)

fun TopicDTO.mapToTopic() = Topic(
    title = name,
    messagesCount = lastMsgId
)

fun Topic.mapToTopicDelegateItem() = TopicDelegateItem(
    name = title,
    msgCount = messagesCount
)

fun TopicDBModel.mapToTopic() = Topic(
    title = title,
    messagesCount = messagesCount
)

fun Topic.mapToTopicDBModel(channelId: Int) = TopicDBModel(
    title = title,
    messagesCount = messagesCount,
    channelId = channelId
)




