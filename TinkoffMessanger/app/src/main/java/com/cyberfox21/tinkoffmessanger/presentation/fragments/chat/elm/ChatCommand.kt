package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import com.cyberfox21.tinkoffmessanger.domain.enum.LoadType

sealed class ChatCommand {
    object LoadCurrentUser : ChatCommand()
    data class LoadMessages(val loadType: LoadType) : ChatCommand()
    object LoadReactionList : ChatCommand()
    data class SendMessage(val msg: String) : ChatCommand()
}
