package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import com.cyberfox21.tinkoffmessanger.domain.usecase.*
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.ChatItemsMapper
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.enums.UpdateType
import com.cyberfox21.tinkoffmessanger.presentation.util.DateFormatterImpl
import com.cyberfox21.tinkoffmessanger.presentation.util.MessageHelperImpl
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vivid.money.elmslie.core.ActorCompat

class ChatActor(
    private val getMessageListUseCase: GetMessageListUseCase,
    private val getMessageUseCase: GetMessageUseCase,
    private val addMessageUseCase: AddMessageUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    private val changeTopicUseCase: ChangeTopicUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val getReactionListUseCase: GetReactionListUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val deleteReactionUseCase: DeleteReactionUseCase,
    private val getMyUserUseCase: GetMyUserUseCase
) : ActorCompat<ChatCommand, ChatEvent> {

    // todo provide dependencies with DI

    private val helper = MessageHelperImpl()

    private val formatter = DateFormatterImpl()

    private val mapper = ChatItemsMapper(helper, formatter)

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
                    mapper.mapToChatDelegateItemsList(messages, command.userId, command.reactions),
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
                    command.channelName,
                    command.topicName,
                    command.loadType,
                    command.lastMessageId
                )
            else getMessageListUseCase(
                command.channelName,
                command.topicName,
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
                command.channelName,
                command.topicName,
                command.loadType,
                command.lastMessageId
            ).subscribeOn(Schedulers.io())
                .map { messagesResult ->
                    messagesResult.fold(
                        { messages ->
                            if (messages.isEmpty()) ChatEvent.Internal.MessagesLoadEmpty
                            else ChatEvent.Internal.MessagesLoadedSuccess(
                                helper.mergeMessages(command.messages, messages),
                                command.updateType
                            )
                        },
                        { ChatEvent.Internal.MessageLoadError(it) }
                    )
                }
        }

        is ChatCommand.LoadMessage -> getMessageUseCase(
            command.channelName,
            command.topicName,
            command.msgId
        ).toObservable().subscribeOn(Schedulers.io())
            .map { result ->
                result.fold({
                    ChatEvent.Internal.MessagesLoadedSuccess(
                        helper.replaceMessage(
                            command.messages,
                            it
                        ), UpdateType.UPDATE
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
            addMessageUseCase(command.channelName, command.topicName, command.msg).subscribeOn(
                Schedulers.io()
            )
                .mapEvents(ChatEvent.Internal.MessageSendingSuccess) { error ->
                    ChatEvent.Internal.MessageSendingError(error)
                }
        }

        is ChatCommand.EditMessage -> {
            editMessageUseCase(command.msgId, command.msg)
                .subscribeOn(Schedulers.io())
                .mapEvents(
                    ChatEvent.Internal.MessageEditingSuccess(command.msgId)
                )
                { ChatEvent.Internal.MessageEditingError(command.msgId, command.msg) }
        }

        is ChatCommand.ChangeMessageTopic -> {
            changeTopicUseCase(command.msgId, command.topic)
                .subscribeOn(Schedulers.io())
                .mapEvents(
                    ChatEvent.Internal.ChangeTopicSuccess(command.msgId),
                    ChatEvent.Internal.ChangeTopicError(command.msgId, command.topic)
                )
        }

        is ChatCommand.DeleteMessage -> deleteMessageUseCase(command.msgId)
            .subscribeOn(Schedulers.io())
            .mapEvents(
                ChatEvent.Internal.MessageDeletingSuccess,
                ChatEvent.Internal.MessageDeletingError(command.msgId)
            )

        is ChatCommand.AddReaction -> addReactionUseCase(command.msgId, command.reaction.name)
            .subscribeOn(Schedulers.io())
            .mapEvents(
                ChatEvent.Internal.ReactionAddingSuccess(command.msgId),
                ChatEvent.Internal.ReactionAddingError
            )

        is ChatCommand.DeleteReaction -> {
            deleteReactionUseCase(command.msgId, command.reaction)
                .subscribeOn(Schedulers.io())
                .mapEvents(
                    ChatEvent.Internal.ReactionDeletingSuccess(command.msgId),
                    ChatEvent.Internal.ReactionDeletingError
                )
        }
    }
}

