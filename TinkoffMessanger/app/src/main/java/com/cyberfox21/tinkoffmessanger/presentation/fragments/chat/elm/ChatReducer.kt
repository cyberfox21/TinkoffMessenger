package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import android.util.Log
import com.cyberfox21.tinkoffmessanger.domain.enums.LoadType
import com.cyberfox21.tinkoffmessanger.presentation.common.ResourceStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.delegate.item.LoadingDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.ButtonSendMode
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.PaginationStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.ScrollStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.UpdateType
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

@Suppress("ControlFlowWithEmptyBody")
class ChatReducer : DslReducer<ChatEvent, ChatState, ChatEffect, ChatCommand>() {
    override fun Result.reduce(event: ChatEvent): Any {
        return when (event) {
            ChatEvent.Ui.GetCurrentUserId -> commands { +ChatCommand.LoadCurrentUser }

            ChatEvent.Ui.GetReactionList -> {
                state { state.copy(reactionsListStatus = ResourceStatus.LOADING) }
                commands { +ChatCommand.LoadReactionList }
            }

            is ChatEvent.Ui.GetMessages -> {
                state {
                    copy(
                        messageStatus = ResourceStatus.LOADING,
                        updateType = event.updateType
                    )
                }
                commands {
                    when (event.updateType) {
                        UpdateType.INITIAL -> +ChatCommand.LoadMessages(
                            loadType = LoadType.ANY,
                            updateType = UpdateType.INITIAL,
                            lastMessageId = if (state.messages.isNotEmpty()) state.messages.last().id
                            else ChatCommand.UNDEFINED_LAST_MESSAGE_ID,
                            state.channelName,
                            state.topicName
                        )
                        UpdateType.RELOAD -> +ChatCommand.LoadMessages(
                            loadType = LoadType.NETWORK,
                            updateType = UpdateType.RELOAD,
                            lastMessageId = if (state.messages.isNotEmpty()) state.messages.last().id
                            else ChatCommand.UNDEFINED_LAST_MESSAGE_ID,
                            state.channelName,
                            state.topicName
                        )
                        UpdateType.PAGINATION -> {
                        }
                        UpdateType.UPDATE -> {
                        }
                    }
                }
            }

            is ChatEvent.Ui.LoadNextMessages -> {
                if (state.messageStatus != ResourceStatus.LOADING
                    && state.paginationStatus != PaginationStatus.FULL
                ) {
                    state {
                        state.copy(
                            messageStatus = ResourceStatus.LOADING,
                            updateType = UpdateType.PAGINATION,
                            chatItems = chatItems + listOf(LoadingDelegateItem()),
                            savedPosition = event.position
                        )
                    }
                    commands {
                        +ChatCommand.LoadNextMessages(
                            LoadType.NETWORK,
                            state.reactions,
                            state.messages,
                            UpdateType.PAGINATION,
                            state.messages.last().id,
                            state.channelName,
                            state.topicName
                        )
                    }
                } else {
                }
            }

            is ChatEvent.Ui.SendMessage -> commands {
                +ChatCommand.SendMessage(event.msg, state.channelName, state.topicName)
            }

            is ChatEvent.Ui.EditMessage -> commands {
                if (state.selectMsgId != null)
                    +ChatCommand.EditMessage(event.text, state.selectMsgId!!)
            }
            is ChatEvent.Ui.ChangeMessageTopic -> commands {
                +ChatCommand.ChangeMessageTopic(event.topic, event.msgId)
            }

            is ChatEvent.Ui.DeleteMessage -> commands { +ChatCommand.DeleteMessage(event.msgId) }

            is ChatEvent.Ui.AddReaction -> {
                commands { +ChatCommand.AddReaction(event.msgId, event.reaction) }
            }

            is ChatEvent.Ui.DeleteReaction -> {
                commands { +ChatCommand.DeleteReaction(event.msgId, event.reaction) }
            }

            ChatEvent.Ui.OnDataInserted -> {
                when (state.scrollStatus) {
                    ScrollStatus.SCROLL_TO_BOTTOM -> effects { +ChatEffect.ScrollToBottom }
                    ScrollStatus.SCROLL_TO_POSITION -> {
                        effects { +ChatEffect.ScrollToPosition(state.savedPosition) }
                    }
                    ScrollStatus.STAY -> effects {}
                }
            }

            is ChatEvent.Internal.UserLoadingSuccess -> {
                state { state.copy(currentUserId = event.user.id) }
                effects { +ChatEffect.PrepareReactionList }
            }

            is ChatEvent.Internal.UserLoadingFailed -> {
                if (state.currentUserId == ChatState.UNDEFINED_USER_ID) {
                    effects { +ChatEffect.ShowNetworkError }
                } else {
                }
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

            is ChatEvent.Internal.MessagesLoadedSuccess -> {
                if (event.updateType == UpdateType.PAGINATION) {
                    if (state.messages.isNotEmpty() &&
                        event.messages.last().id == state.messages.last().id
                    ) {
                        state { copy(paginationStatus = PaginationStatus.FULL) }
                    } else {
                        state { copy(paginationStatus = PaginationStatus.PART) }
                    }
                }
                commands {
                    +ChatCommand.LoadChatItems(
                        state.currentUserId,
                        state.reactions,
                        event.messages,
                        event.updateType,
                        state.channelName,
                        state.topicName
                    )
                }
            }

            ChatEvent.Internal.MessagesLoadEmpty -> {
                if (state.chatItems.isEmpty()) {
                    state { state.copy(messageStatus = ResourceStatus.EMPTY) }
                } else {
                    state { state.copy(messageStatus = ResourceStatus.SUCCESS) }
                }
            }

            is ChatEvent.Internal.MessageLoadError -> {
                if (state.chatItems.isEmpty()) {
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

            is ChatEvent.Internal.ChatItemsLoadedSuccess -> {
                state {
                    state.copy(
                        messages = event.messages.distinctBy { it.id },
                        chatItems = event.items,
                        messageStatus = ResourceStatus.SUCCESS
                    )
                }
                if (event.messages.isNotEmpty()) {
                    when (event.updateType) {
                        UpdateType.INITIAL -> {
                            state { copy(scrollStatus = ScrollStatus.SCROLL_TO_BOTTOM) }
                            effects { +ChatEffect.ScrollToBottom }
                        }
                        UpdateType.RELOAD -> {
                            state { copy(scrollStatus = ScrollStatus.SCROLL_TO_POSITION) }
                        }
                        UpdateType.PAGINATION -> {
                            state { copy(scrollStatus = ScrollStatus.SCROLL_TO_POSITION) }
                        }
                        UpdateType.UPDATE -> {

                        }
                    }
                } else {
                    Log.d("ChatReducer", "Messages list is empty")
                }

            }

            is ChatEvent.Internal.ChatItemsLoadedError -> {
                state {
                    state.copy(
                        messageStatus = ResourceStatus.ERROR,
                        messageError = event.error
                    )
                }
            }

            ChatEvent.Internal.MessageSendingSuccess -> {
                state {
                    copy(
                        savedPosition = 0,
                        messageStatus = ResourceStatus.LOADING,
                        updateType = UpdateType.RELOAD
                    )
                }
                commands {
                    +ChatCommand.LoadMessages(
                        LoadType.NETWORK,
                        updateType = UpdateType.RELOAD,
                        ChatCommand.UNDEFINED_LAST_MESSAGE_ID,
                        state.channelName,
                        state.topicName
                    )
                }
                effects { +ChatEffect.MessageSendingSuccess }
            }

            is ChatEvent.Internal.MessageSendingError -> effects { +ChatEffect.MessageSendingError }

            is ChatEvent.Internal.MessageEditingSuccess -> {
                state {
                    copy(
                        btnSendMode = ButtonSendMode.ADD,
                        updateType = UpdateType.UPDATE
                    )
                }
                commands {
                    +ChatCommand.LoadMessage(
                        state.currentUserId,
                        state.reactions,
                        state.messages,
                        event.msgId,
                        state.channelName,
                        state.topicName
                    )
                }
                effects { +ChatEffect.MessageEditingSuccess }
            }
            is ChatEvent.Internal.MessageEditingError -> effects {
                +ChatEffect.MessageEditingError(event.msgId, event.text)
            }

            is ChatEvent.Internal.ChangeTopicSuccess -> commands {
                +ChatCommand.LoadMessages(
                    LoadType.NETWORK,
                    updateType = UpdateType.RELOAD,
                    ChatCommand.UNDEFINED_LAST_MESSAGE_ID,
                    state.channelName,
                    state.topicName
                )
            }

            is ChatEvent.Internal.ChangeTopicError -> effects {
                +ChatEffect.ChangeTopicError(event.msgId, event.topic)
            }

            ChatEvent.Internal.MessageDeletingSuccess -> {
                state {
                    copy(
                        savedPosition = 0,
                        messageStatus = ResourceStatus.LOADING,
                        updateType = UpdateType.RELOAD
                    )
                }
                commands {
                    +ChatCommand.LoadMessages(
                        LoadType.NETWORK,
                        updateType = UpdateType.RELOAD,
                        ChatCommand.UNDEFINED_LAST_MESSAGE_ID,
                        state.channelName,
                        state.topicName
                    )
                }
            }

            is ChatEvent.Internal.MessageDeletingError -> effects {
                +ChatEffect.MessageDeletingError(event.msgId)
            }

            ChatEvent.Internal.ReactionAddingError -> effects { +ChatEffect.EmojiAddedError }
            is ChatEvent.Internal.ReactionAddingSuccess -> {
                commands {
                    +ChatCommand.LoadMessage(
                        state.currentUserId,
                        state.reactions,
                        state.messages,
                        event.msgId,
                        state.channelName,
                        state.topicName
                    )
                }
            }

            ChatEvent.Internal.ReactionDeletingError -> effects { +ChatEffect.EmojiDeletedError }
            is ChatEvent.Internal.ReactionDeletingSuccess -> {
                commands {
                    +ChatCommand.LoadMessage(
                        state.currentUserId,
                        state.reactions,
                        state.messages,
                        event.msgId,
                        state.channelName,
                        state.topicName
                    )
                }

            }
        }
    }
}
