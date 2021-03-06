package com.cyberfox21.tinkoffmessanger.data

import androidx.core.text.HtmlCompat
import com.cyberfox21.tinkoffmessanger.data.database.model.*
import com.cyberfox21.tinkoffmessanger.data.network.response.dto.*
import com.cyberfox21.tinkoffmessanger.domain.entity.*
import com.cyberfox21.tinkoffmessanger.domain.enums.UserStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.MessageReactionListItem
import com.cyberfox21.tinkoffmessanger.presentation.util.DateFormatterImpl
import com.cyberfox21.tinkoffmessanger.presentation.util.EmojiFormatter.codeToEmoji

private val formatter = DateFormatterImpl()

fun MessageDTO.mapToMessage() = Message(
    id = id,
    message = HtmlCompat.fromHtml(content, 0).toString(),
    time = formatter.utcToDate(timestamp),
    senderId = senderId,
    senderName = senderFullName,
    senderAvatarUrl = avatarUrl,
    isCurrentUser = isMeMessage,
    reactions = reactions.map {
        it.mapToReaction()
    }
)

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

fun Message.mapToMessageDBModel(topic: String) = MessageDBModel(
    id = id,
    message = message,
    time = time,
    senderId = senderId,
    senderName = senderName,
    senderAvatarUrl = senderAvatarUrl,
    isCurrentUser = isCurrentUser,
    reactions = reactions,
    topicName = topic
)

fun SubscribedChannelDTO.mapToChannel() = Channel(
    id = streamId,
    name = name,
)

fun ChannelDTO.mapToChannel() = Channel(
    id = streamId,
    name = name,
)

fun Channel.mapToChannelDBModel() = ChannelDBModel(
    id = id,
    name = name
)

fun ChannelDBModel.mapToChannel() = Channel(
    id = id,
    name = name
)

fun Channel.mapToSubscribedChannelDBModel() = SubscribedChannelDBModel(
    id = id,
    name = name
)

fun SubscribedChannelDBModel.mapToChannel() = Channel(
    id = id,
    name = name
)

fun TopicDTO.mapToTopic() = Topic(
    title = name,
    messagesCount = lastMsgId
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

fun Reaction.mapToReactionListDBModel() = ReactionListDBModel(
    userId = userId,
    code = code,
    name = name,
    reaction = reaction,
    type = type
)

fun ReactionListDBModel.mapToReactionForReactionList() = Reaction(
    userId = userId,
    code = code,
    name = name,
    reaction = reaction,
    type = type
)

fun ReactionDTO.mapToReaction() = Reaction(
    userId = userId,
    code = code,
    name = name,
    reaction = codeToEmoji(code),
    type = type
)

fun MessageReactionListItem.mapToReaction() = Reaction(
    userId = Reaction.UNDEFINED_ID,
    code = code,
    name = name,
    reaction = reaction,
    type = type
)

fun UserDBModel.mapToUser() = User(
    id = id,
    avatar = avatar,
    name = name,
    email = email,
    status = status
)

fun CurrentUserDBModel.mapToUser() = User(
    id = id,
    avatar = avatar,
    name = name,
    email = email,
    status = status
)

fun User.mapToUserDBModel() = UserDBModel(
    id = id,
    avatar = avatar,
    name = name,
    email = email,
    status = status
)

fun User.mapToCurrentUserDBModel() = CurrentUserDBModel(
    id = id,
    avatar = avatar,
    name = name,
    email = email,
    status = status
)

fun UserDTO.mapToUser() = User(
    id = id,
    avatar = avatar_url,
    name = full_name,
    email = email,
    status = is_active
)

fun UserPresenceClientDTO.mapToStatus(): UserStatus {
    return when (status) {
        UserStatus.ONLINE.apiName -> UserStatus.ONLINE
        UserStatus.IDLE.apiName -> UserStatus.IDLE
        else -> UserStatus.OFFLINE
    }
}
