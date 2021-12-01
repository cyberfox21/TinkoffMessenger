package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction

sealed class ChatEvent {
    sealed class Ui : ChatEvent() {
        object GetMessages : Ui()
        object GetReactionList : Ui()
        data class SendMessage(val msg: Message) : Ui()
    }

    sealed class Internal : ChatEvent() {
        data class MessagesLoaded(val messages: List<Message>) : Internal()
        data class MessageLoadError(val error: Throwable) : Internal()
        data class ReactionsLoaded(val reactions: List<Reaction>) : Internal()
        data class ReactionsLoadError(val error: Throwable) : Internal()
    }
}