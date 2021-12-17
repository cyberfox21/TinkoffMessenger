package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

sealed class ChatEffect {
    data class MessagesLoadError(val error: Throwable) : ChatEffect()
    object EmptyMessageList : ChatEffect()
    data class ReactionsLoadError(val error: Throwable) : ChatEffect()
    object EmptyReactionList : ChatEffect()

    object MessageSendingError : ChatEffect()
    object MessageSendingSuccess : ChatEffect()
}
