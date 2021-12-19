package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

sealed class ChatEffect {

    object PrepareReactionList : ChatEffect()
    object StartChatFragmentWork : ChatEffect()

    object MessageSendingError : ChatEffect()

    object EmojiAddedSuccess : ChatEffect()
    object EmojiAddedError : ChatEffect()

    object EmojiDeletedSuccess : ChatEffect()
    object EmojiDeletedError : ChatEffect()
}
