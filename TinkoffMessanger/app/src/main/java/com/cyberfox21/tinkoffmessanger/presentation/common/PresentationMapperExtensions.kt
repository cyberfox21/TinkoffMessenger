package com.cyberfox21.tinkoffmessanger.presentation.common

import android.text.SpannableString
import androidx.core.text.HtmlCompat
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.ChannelDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.TopicDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.*
import com.cyberfox21.tinkoffmessanger.presentation.util.DateFormatter

fun String.mapMessageContent(emojis: List<Reaction>): SpannableString {
    return SpannableString(
        HtmlCompat.fromHtml(
            if (this.contains(":")) {
                var isFirstColon = false
                this.split(":").joinToString("") {
                    val emoji = emojis.find { emoji -> emoji.name == it }
                    val text = if (isFirstColon) ":$it" else it
                    isFirstColon = emoji?.reaction == null
                    emoji?.reaction ?: text
                }
            } else {
                this
            },
            HtmlCompat.FROM_HTML_MODE_COMPACT
        ).trim()
    )
}

fun List<Message>.replaceMessage(newMessage: Message): List<Message> {
    return this.toMutableList().apply {
        val index = this.indexOfFirst { message -> message.id == newMessage.id }
        removeAt(index)
        addAll(index, listOf(newMessage))
    }
}

fun List<Message>.mergeMessages(newMessages: List<Message>): List<Message> {
    return this.toMutableList().apply {
        removeAt(this.lastIndex)
        addAll(newMessages)
    }
}

fun List<Message>.toDelegateChatItemsList(
    userId: Int,
    reactionList: List<Reaction>
): List<ChatDelegateItem> {
    val delegateItemList = mutableListOf<ChatDelegateItem>()
    this.indices.forEach { index ->
        val message = this[index]
        val reactions = message.reactions

        val currentUserReactions = reactions
            .filter { it.userId == userId }
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

        val listMessage = when (message.senderId == userId) {
            true -> {
                MyMessageDelegateItem(
                    message.id,
                    message.message.mapMessageContent(reactionList),
                    DateFormatter.getTimeForMessage(message.time),
                    listReactions
                )
            }
            false -> {
                AlienMessageDelegateItem(
                    message.id,
                    message.message.mapMessageContent(reactionList),
                    DateFormatter.getTimeForMessage(message.time),
                    message.senderId,
                    message.senderName,
                    message.senderAvatarUrl,
                    listReactions
                )
            }
        }
        delegateItemList.add(listMessage)

        if (index == lastIndex || !DateFormatter.isTheSameDay(message.time, this[index + 1].time)) {
            delegateItemList.add(DateDelegateItem(DateFormatter.getDateForChatItem(message.time)))
        }
    }
    return delegateItemList
}

fun Channel.mapToChannelDelegateItem(selected: Boolean) = ChannelDelegateItem(
    id = id,
    name = name,
    isSelected = selected
)

fun Topic.mapToTopicDelegateItem() = TopicDelegateItem(
    name = title,
    msgCount = messagesCount
)
