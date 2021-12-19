package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

sealed class ChatEffect {

    object PrepareReactionList : ChatEffect()
    object StartChatFragmentWork : ChatEffect()

    object MessageSendingError : ChatEffect()
    object MessageSendingSuccess : ChatEffect()

    object EmojiAddedSuccess : ChatEffect()
    object EmojiAddedError : ChatEffect()

    object EmojiDeletedSuccess : ChatEffect()
    object EmojiDeletedError : ChatEffect()

    object ShowNetworkError : ChatEffect()

    object ScrollToTop : ChatEffect()
    data class ScrollToPosition(val position: Int) : ChatEffect()
}
