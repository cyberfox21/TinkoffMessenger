package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import com.cyberfox21.tinkoffmessanger.domain.enum.LoadType
import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ChatReducer : DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {
    override fun Result.reduce(event: ChatEvent): Any {
        return when (event) {
            is ChatEvent.Internal.MessagesLoaded -> {
                state {
                    state.copy(
                        messages = event.messages,
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
            is ChatEvent.Internal.ReactionsLoaded -> {
                state {
                    state.copy(
                        reactions = event.reactions,
                        reactionsListStatus = ResourceStatus.SUCCESS
                    )
                }
            }
            is ChatEvent.Internal.ReactionsLoadError -> {
                state {
                    state.copy(
                        reactionsListStatus = ResourceStatus.ERROR,
                        reactionsError = event.error
                    )
                }
            }
            ChatEvent.Ui.GetMessages -> {
                state { state.copy(messageStatus = ResourceStatus.LOADING) }
                commands { +ChatCommand.LoadMessages(LoadType.ANY) }
            }
            ChatEvent.Ui.GetReactionList -> {
                state { state.copy(reactionsListStatus = ResourceStatus.LOADING) }
                commands { +ChatCommand.LoadReactionList }
            }
            is ChatEvent.Ui.SendMessage -> commands { +ChatCommand.SendMessage(event.msg) }
            is ChatEvent.Ui.AddReaction -> {
            }
            ChatEvent.Internal.MessageSendingSuccess -> commands {
                +ChatCommand.LoadMessages(LoadType.NETWORK)
            }
            is ChatEvent.Internal.MessageSendingError -> effects { +ChatEffect.MessageSendingError }

        }
    }
}
