package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import com.cyberfox21.tinkoffmessanger.domain.usecase.*
import com.cyberfox21.tinkoffmessanger.presentation.common.mergeMessages
import com.cyberfox21.tinkoffmessanger.presentation.common.replaceMessage
import com.cyberfox21.tinkoffmessanger.presentation.common.toDelegateChatItemsList
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.UpdateType
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vivid.money.elmslie.core.ActorCompat

class ChatActor(
    private val getMessageListUseCase: GetMessageListUseCase,
    private val getMessageUseCase: GetMessageUseCase,
    private val addMessageUseCase: AddMessageUseCase,
    private val getReactionListUseCase: GetReactionListUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val deleteReactionUseCase: DeleteReactionUseCase,
    private val getMyUserUseCase: GetMyUserUseCase
) : ActorCompat<ChatCommand, ChatEvent> {

    lateinit var channelName: String
    lateinit var topicName: String

    override fun execute(command: ChatCommand): Observable<ChatEvent> = when (command) {
        ChatCommand.LoadCurrentUser -> getMyUserUseCase().subscribeOn(Schedulers.io())
            .map { result ->
                result.fold(
                    { user -> ChatEvent.Internal.UserLoadingSuccess(user) },
                    { error -> ChatEvent.Internal.UserLoadingFailed(error) }
                )
            }

        is ChatCommand.LoadChatItems -> {
            Observable.just(command.messages).map { messages ->
                Pair(
                    messages.toDelegateChatItemsList(command.userId, command.reactions),
                    command.messages
                )
            }.mapEvents(
                { items ->
                    ChatEvent.Internal.ChatItemsLoadedSuccess(
                        items.first,
                        items.second,
                        command.updateType
                    )
                },
                { error -> ChatEvent.Internal.ChatItemsLoadedError(error) }
            )
        }

        is ChatCommand.LoadMessages -> {
            (if (command.lastMessageId != ChatCommand.UNDEFINED_LAST_MESSAGE_ID)
                getMessageListUseCase(
                    channelName,
                    topicName,
                    command.loadType,
                    command.lastMessageId
                )
            else getMessageListUseCase(
                channelName,
                topicName,
                command.loadType,
                ChatCommand.UNDEFINED_LAST_MESSAGE_ID
            ))
                .subscribeOn(Schedulers.io())
                .map { messagesResult ->
                    messagesResult.fold(
                        { messages ->
                            if (messages.isEmpty()) ChatEvent.Internal.MessagesLoadEmpty
                            else ChatEvent.Internal.MessagesLoadedSuccess(
                                messages,
                                command.updateType
                            )
                        },
                        { ChatEvent.Internal.MessageLoadError(it) }
                    )
                }
        }

        is ChatCommand.LoadNextMessages -> {
            getMessageListUseCase(
                channelName,
                topicName,
                command.loadType,
                command.lastMessageId
            ).subscribeOn(Schedulers.io())
                .map { messagesResult ->
                    messagesResult.fold(
                        { messages ->
                            if (messages.isEmpty()) ChatEvent.Internal.MessagesLoadEmpty
                            else ChatEvent.Internal.MessagesLoadedSuccess(
                                command.messages.mergeMessages(messages),
                                command.updateType
                            )
                        },
                        { ChatEvent.Internal.MessageLoadError(it) }
                    )
                }
        }

        is ChatCommand.LoadMessage -> getMessageUseCase(
            channelName,
            topicName,
            command.msgId
        ).toObservable().subscribeOn(Schedulers.io())
            .map { result ->
                result.fold({
                    ChatEvent.Internal.MessagesLoadedSuccess(
                        command.messages.replaceMessage(it),
                        UpdateType.UPDATE
                    )
                }, { ChatEvent.Internal.ReactionAddingError }
                )
            }

        ChatCommand.LoadReactionList -> {
            getReactionListUseCase()
                .subscribeOn(Schedulers.io())
                .mapEvents(
                    { reactions -> ChatEvent.Internal.ReactionsLoaded(reactions) },
                    { error -> ChatEvent.Internal.ReactionsLoadError(error) }
                )
        }
        is ChatCommand.SendMessage -> {
            addMessageUseCase(channelName, topicName, command.msg).subscribeOn(Schedulers.io())
                .mapEvents(ChatEvent.Internal.MessageSendingSuccess) { error ->
                    ChatEvent.Internal.MessageSendingError(error)
                }
        }
        is ChatCommand.AddReaction -> addReactionUseCase(command.msgId, command.reaction.name)
            .subscribeOn(Schedulers.io())
            .mapEvents(
                ChatEvent.Internal.ReactionAddingSuccess(command.msgId),
                ChatEvent.Internal.ReactionAddingError
            )
        is ChatCommand.DeleteReaction -> {
            deleteReactionUseCase(command.msgId, command.reaction).subscribeOn(Schedulers.io())
                .mapEvents(
                    ChatEvent.Internal.ReactionDeletingSuccess(command.msgId),
                    ChatEvent.Internal.ReactionDeletingError
                )
        }

    }
}

