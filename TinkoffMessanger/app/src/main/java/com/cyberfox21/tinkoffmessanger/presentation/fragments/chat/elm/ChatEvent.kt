package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.entity.User

sealed class ChatEvent {
    sealed class Ui : ChatEvent() {
        object GetCurrentUserId : Ui()

        data class GetMessages(val needLoading: Boolean) : Ui()
        object GetReactionList : Ui()
        data class SendMessage(val msg: String) : Ui()

        data class AddReaction(val reaction: Reaction, val msgId: Int) : Ui()
        data class DeleteReaction(val reaction: Reaction, val msgId: Int) : Ui()
    }

    sealed class Internal : ChatEvent() {
        data class UserLoadingSuccess(val user: User) : Internal()
        data class UserLoadingFailed(val error: Throwable) : Internal()

        data class ReactionsLoaded(val reactions: List<Reaction>) : Internal()
        data class ReactionsLoadError(val error: Throwable) : Internal()

        data class MessagesLoaded(val messages: List<Message>) : Internal()
        object MessagesLoadEmpty : Internal()
        data class MessageLoadError(val error: Throwable) : Internal()

        object MessageSendingSuccess : Internal()
        data class MessageSendingError(val error: Throwable) : Internal()

        object ReactionAddingSuccess : Internal()
        object ReactionAddingError : Internal()

        object ReactionDeletingSuccess : Internal()
        object ReactionDeletingError : Internal()

    }
}
