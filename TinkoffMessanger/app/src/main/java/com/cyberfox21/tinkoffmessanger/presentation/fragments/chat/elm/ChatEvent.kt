package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.ChatDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.UpdateType

sealed class ChatEvent {
    sealed class Ui : ChatEvent() {
        object GetCurrentUserId : Ui()

        data class GetMessages(val updateType: UpdateType) : Ui()
        object GetReactionList : Ui()

        data class SendMessage(val msg: String) : Ui()
        data class EditMessage(val text: String) : Ui()
        data class ChangeMessageTopic(val msgId: Int, val topic: String) : Ui()
        data class DeleteMessage(val msgId: Int) : Ui()

        data class LoadNextMessages(val position: Int) : Ui()

        data class AddReaction(val reaction: Reaction, val msgId: Int) : Ui()
        data class DeleteReaction(val reaction: Reaction, val msgId: Int) : Ui()

        object OnDataInserted : Ui()
    }

    sealed class Internal : ChatEvent() {
        data class UserLoadingSuccess(val user: User) : Internal()
        data class UserLoadingFailed(val error: Throwable) : Internal()

        data class ReactionsLoaded(val reactions: List<Reaction>) : Internal()
        data class ReactionsLoadError(val error: Throwable) : Internal()

        data class MessagesLoadedSuccess(val messages: List<Message>, val updateType: UpdateType) :
            Internal()

        object MessagesLoadEmpty : Internal()
        data class MessageLoadError(val error: Throwable) : Internal()

        data class ChatItemsLoadedSuccess(
            val items: List<ChatDelegateItem>,
            val messages: List<Message>,
            val updateType: UpdateType
        ) : Internal()

        data class ChatItemsLoadedError(val error: Throwable) : Internal()

        object MessageSendingSuccess : Internal()
        data class MessageSendingError(val error: Throwable) : Internal()

        object MessageDeletingSuccess : Internal()
        data class MessageDeletingError(val msgId: Int) : Internal()

        data class MessageEditingSuccess(val msgId: Int) : Internal()
        data class MessageEditingError(val msgId: Int, val text: String) : Internal()

        data class ChangeTopicSuccess(val msgId: Int) : Internal()
        data class ChangeTopicError(val msgId: Int, val topic: String) : Internal()

        data class ReactionAddingSuccess(val msgId: Int) : Internal()
        object ReactionAddingError : Internal()

        data class ReactionDeletingSuccess(val msgId: Int) : Internal()
        object ReactionDeletingError : Internal()
    }
}
