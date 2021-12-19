package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import android.util.Log
import com.cyberfox21.tinkoffmessanger.domain.enum.LoadType
import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import com.cyberfox21.tinkoffmessanger.presentation.toDelegateChatItemsList
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ChatReducer : DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {
    override fun Result.reduce(event: ChatEvent): Any {
        return when (event) {
            ChatEvent.Ui.GetCurrentUserId -> commands { +ChatCommand.LoadCurrentUser }

            ChatEvent.Ui.GetReactionList -> {
                state { state.copy(reactionsListStatus = ResourceStatus.LOADING) }
                commands { +ChatCommand.LoadReactionList }
            }

            is ChatEvent.Ui.GetMessages -> {
                if (event.needLoading) {
                    state { state.copy(messageStatus = ResourceStatus.LOADING) }
                    commands { +ChatCommand.LoadMessages(LoadType.ANY) }
                } else {
                    commands { +ChatCommand.LoadMessages(LoadType.NETWORK) }
                }
            }

            is ChatEvent.Ui.SendMessage -> commands { +ChatCommand.SendMessage(event.msg) }

            is ChatEvent.Ui.AddReaction -> {
                commands { +ChatCommand.AddReaction(event.msgId, event.reaction) }
            }

            is ChatEvent.Ui.DeleteReaction -> {
                commands { +ChatCommand.DeleteReaction(event.msgId, event.reaction) }
            }

            is ChatEvent.Internal.UserLoadingSuccess -> {
                state { state.copy(currentUserId = event.user.id) }
                effects { +ChatEffect.PrepareReactionList }
            }

            is ChatEvent.Internal.UserLoadingFailed -> {
                if (state.currentUserId == ChatState.UNDEFINED_USER_ID) {
                    effects { +ChatEffect.ShowNetworkError }
                } else { }
            }

            is ChatEvent.Internal.ReactionsLoaded -> {
                state {
                    state.copy(
                        reactions = event.reactions,
                        reactionsListStatus = ResourceStatus.SUCCESS
                    )
                }
                effects { +ChatEffect.StartChatFragmentWork }
            }

            is ChatEvent.Internal.ReactionsLoadError -> {
                if (state.reactions.isEmpty()) {
                    state {
                        state.copy(
                            reactionsListStatus = ResourceStatus.ERROR,
                            reactionsError = event.error
                        )
                    }
                    effects { +ChatEffect.ShowNetworkError }
                } else {
                    state { state.copy(reactionsListStatus = ResourceStatus.SUCCESS) }
                }
            }

            is ChatEvent.Internal.MessagesLoaded -> {
                state {
                    state.copy(
                        messages = event.messages.toDelegateChatItemsList(
                            state.currentUserId,
                            state.reactions
                        ),
                        messageStatus = ResourceStatus.SUCCESS
                    )
                }
            }

            ChatEvent.Internal.MessagesLoadEmpty -> {
                if (state.messages.isEmpty()) {
                    state { state.copy(messageStatus = ResourceStatus.EMPTY) }
                } else {
                    state { state.copy(messageStatus = ResourceStatus.SUCCESS) }
                }
            }

            is ChatEvent.Internal.MessageLoadError -> {
                if (state.messages.isEmpty()) {
                    state {
                        state.copy(
                            messageStatus = ResourceStatus.ERROR,
                            messageError = event.error
                        )
                    }
                } else {
                    state { state.copy(messageStatus = ResourceStatus.SUCCESS) }
                }
            }


            ChatEvent.Internal.MessageSendingSuccess -> commands {
                +ChatCommand.LoadMessages(LoadType.NETWORK)
            }

            is ChatEvent.Internal.MessageSendingError -> effects { +ChatEffect.MessageSendingError }

            ChatEvent.Internal.ReactionAddingError -> effects { +ChatEffect.EmojiAddedError }
            ChatEvent.Internal.ReactionAddingSuccess -> effects { +ChatEffect.EmojiAddedSuccess }

            ChatEvent.Internal.ReactionDeletingError -> effects { +ChatEffect.EmojiDeletedError }
            ChatEvent.Internal.ReactionDeletingSuccess -> effects { +ChatEffect.EmojiDeletedSuccess }
        }
    }
}
