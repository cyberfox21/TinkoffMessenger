package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

sealed class ChatEffect {

    object PrepareReactionList : ChatEffect()
    object StartChatFragmentWork : ChatEffect()

    object MessageSendingError : ChatEffect()
    object MessageSendingSuccess : ChatEffect()

    object MessageEditingSuccess : ChatEffect()
    data class MessageEditingError(val msgId: Int, val text: String) : ChatEffect()

    data class ChangeTopicError(val msgId: Int, val topic: String) : ChatEffect()

    data class MessageDeletingError(val messageId: Int) : ChatEffect()

    object EmojiAddedError : ChatEffect()

    object EmojiDeletedError : ChatEffect()

    object ShowNetworkError : ChatEffect()

    object ScrollToBottom : ChatEffect()
    data class ScrollToPosition(val position: Int) : ChatEffect()
}
