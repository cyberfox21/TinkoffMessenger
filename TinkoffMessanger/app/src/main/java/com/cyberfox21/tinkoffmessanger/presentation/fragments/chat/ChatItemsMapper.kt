package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.*
import com.cyberfox21.tinkoffmessanger.presentation.util.DateFormatter
import com.cyberfox21.tinkoffmessanger.presentation.util.MessageHelper

class ChatItemsMapper(private val helper: MessageHelper, private val formatter: DateFormatter) {

    fun mapToChatDelegateItemsList(
        messages: List<Message>,
        userId: Int,
        reactionList: List<Reaction>
    ): List<ChatDelegateItem> {
        val delegateItemList = mutableListOf<ChatDelegateItem>()
        messages.indices.forEach { index ->

            val message = messages[index]

            val listReactions = mapToReactionsListItem(message)

            delegateItemList.add(mapToMessageListItem(message, reactionList, listReactions, userId))

            checkIsDateNeeded(messages, index, delegateItemList)

        }
        return delegateItemList
    }

    private fun mapToReactionsListItem(message: Message): List<MessageReactionListItem> {
        val reactions = message.reactions

        val currentUserReactions = reactions
            .filter { it.userId == message.senderId }
            .map { it.reaction }

        val listReactions = mutableListOf<MessageReactionListItem>()

        reactions.distinctBy { it.reaction }.forEach {
            listReactions.add(
                MessageReactionListItem(
                    name = it.name,
                    code = it.code,
                    type = it.type,
                    senderId = it.userId,
                    reaction = it.reaction,
                    count = reactions.count { msgEmoji -> msgEmoji.reaction == it.reaction },
                    isSelected = currentUserReactions.contains(it.reaction)
                )
            )
        }
        listReactions.sortByDescending { it.count }
        return listReactions.toList()
    }

    private fun mapToMessageListItem(
        message: Message,
        reactionList: List<Reaction>,
        listReactions: List<MessageReactionListItem>,
        userId: Int
    ): MessageDelegateItem {
        val messageContent = helper.mapMessageContent(message.message, reactionList).toString()
        return when (message.senderId == userId) {
            true -> MyMessageDelegateItem(
                id = message.id,
                myId = userId,
                text = messageContent,
                timeAsId = message.time.time,
                time = formatter.getTimeForMessage(message.time),
                reactions = listReactions
            )
            false ->
                AlienMessageDelegateItem(
                    id = message.id,
                    text = messageContent,
                    timeAsId = message.time.time,
                    time = formatter.getTimeForMessage(message.time),
                    senderId = message.senderId,
                    senderName = message.senderName,
                    senderAvatarUrl = message.senderAvatarUrl,
                    reactions = listReactions
                )
        }
    }

    private fun checkIsDateNeeded(
        messages: List<Message>,
        index: Int,
        chatItemsList: MutableList<ChatDelegateItem>
    ) {
        if (index == messages.lastIndex ||
            !formatter.isTheSameDay(messages[index].time, messages[index + 1].time)
        ) chatItemsList.add(DateDelegateItem(formatter.getDateForChatItem(messages[index].time)))

    }
}
