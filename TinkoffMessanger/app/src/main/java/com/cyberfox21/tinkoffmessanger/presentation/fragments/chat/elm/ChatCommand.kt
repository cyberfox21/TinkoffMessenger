package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.Message

sealed class ChatCommand {
    object LoadMessages : ChatCommand()
    object LoadReactionList : ChatCommand()
    data class SendMessage(val msg: Message) : ChatCommand()
}