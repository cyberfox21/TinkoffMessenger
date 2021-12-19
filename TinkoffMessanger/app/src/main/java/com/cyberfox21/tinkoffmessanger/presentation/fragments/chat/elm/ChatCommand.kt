package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.enum.LoadType

sealed class ChatCommand {
    object LoadCurrentUser : ChatCommand()
    data class LoadMessages(val loadType: LoadType) : ChatCommand()
    object LoadReactionList : ChatCommand()
    data class SendMessage(val msg: String) : ChatCommand()

    data class AddReaction(val msgId: Int, val reaction: Reaction) : ChatCommand()
    data class DeleteReaction(val msgId: Int, val reaction: Reaction) : ChatCommand()
}
