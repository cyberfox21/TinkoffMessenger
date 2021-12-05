package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ChatReducer : DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {
    override fun Result.reduce(event: ChatEvent): Any {
        return when (event) {
            is ChatEvent.Internal.MessagesLoaded -> {
                state {
                    if(event.messages.isEmpty() && this.messages.isNotEmpty()){
                        state.copy(
                            isMessagesLoading = false,
                            messageError = null
                        )
                    }else{
                        state.copy(
                            messages = event.messages,
                            isEmptyMessageList = event.messages.isEmpty(),
                            isMessagesLoading = false,
                            messageError = null
                        )
                    }
                }
                effects { if (state.isEmptyMessageList) ChatEffect.EmptyMessageList }
            }
            is ChatEvent.Internal.MessageLoadError -> {
                state {
                    state.copy(
                        isMessagesLoading = false,
                        messageError = event.error
                    )
                }
                effects { ChatEffect.MessagesLoadError(event.error) }
            }
            is ChatEvent.Internal.ReactionsLoaded -> {
                state {
                    state.copy(
                        reactions = event.reactions,
                        isEmptyReactionList = event.reactions.isEmpty(),
                        isReactionsLoading = false,
                        reactionsError = null
                    )
                }
                effects { if (state.isEmptyReactionList) ChatEffect.EmptyReactionList }
            }
            is ChatEvent.Internal.ReactionsLoadError -> {
                state {
                    state.copy(
                        isReactionsLoading = false,
                        reactionsError = event.error
                    )
                }
                effects { ChatEffect.ReactionsLoadError(event.error) }
            }
            ChatEvent.Ui.GetMessages -> {
                state { state.copy(isMessagesLoading = true) }
                commands { +ChatCommand.LoadMessages }
            }
            ChatEvent.Ui.GetReactionList -> {
                state { state.copy(isReactionsLoading = true) }
                commands { +ChatCommand.LoadReactionList }
            }
            is ChatEvent.Ui.SendMessage -> {
                // todo fix this
                state { state.copy(isMessagesLoading = true) }
                commands { +ChatCommand.SendMessage(event.msg) }
            }
            is ChatEvent.Ui.AddReaction -> {

            }
        }
    }
}