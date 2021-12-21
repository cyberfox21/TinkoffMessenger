package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.enums.LoadType
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.UpdateType

sealed class ChatCommand {
    object LoadCurrentUser : ChatCommand()

    data class LoadChatItems(
        val userId: Int,
        var reactions: List<Reaction>,
        val messages: List<Message>,
        val updateType: UpdateType
    ) : ChatCommand()

    data class LoadMessages(
        val loadType: LoadType,
        val updateType: UpdateType,
        val lastMessageId: Int
    ) : ChatCommand()

    data class LoadNextMessages(
        val loadType: LoadType,
        var reactions: List<Reaction>,
        val messages: List<Message>,
        val updateType: UpdateType,
        val lastMessageId: Int
    ) : ChatCommand()

    data class LoadMessage(
        val userId: Int,
        var reactions: List<Reaction>,
        val messages: List<Message>,
        val msgId: Int
    ) : ChatCommand()

    object LoadReactionList : ChatCommand()

    data class SendMessage(val msg: String) : ChatCommand()

    data class EditMessage(val msg: String, val msgId: Int) : ChatCommand()

    data class ChangeMessageTopic(val topic: String, val msgId: Int) : ChatCommand()

    data class DeleteMessage(val msgId: Int) : ChatCommand()

    data class AddReaction(val msgId: Int, val reaction: Reaction) : ChatCommand()
    data class DeleteReaction(val msgId: Int, val reaction: Reaction) : ChatCommand()

    companion object {
        const val UNDEFINED_LAST_MESSAGE_ID = -1
    }
}
