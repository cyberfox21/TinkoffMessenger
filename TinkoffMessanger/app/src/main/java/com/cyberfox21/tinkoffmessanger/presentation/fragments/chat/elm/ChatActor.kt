package com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.elm

import com.cyberfox21.tinkoffmessanger.domain.usecase.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vivid.money.elmslie.core.ActorCompat

class ChatActor(
    private val getMessageListUseCase: GetMessageListUseCase,
    private val addMessageUseCase: AddMessageUseCase,
    private val getReactionListUseCase: GetReactionListUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val deleteReactionUseCase: DeleteReactionUseCase,
    private val getMyUserUseCase: GetMyUserUseCase
) : ActorCompat<ChatCommand, ChatEvent> {

    private var numBefore: Int = 100
    private var numAfter: Int = 0

    lateinit var channelName: String
    lateinit var topicName: String

    override fun execute(command: ChatCommand): Observable<ChatEvent> = when (command) {
        is ChatCommand.LoadMessages -> {
            getMessageListUseCase(numBefore, numAfter, channelName, topicName, command.loadType)
                .subscribeOn(Schedulers.io())
                .mapEvents(
                    { messages -> ChatEvent.Internal.MessagesLoaded(messages) },
                    { error -> ChatEvent.Internal.MessageLoadError(error) }
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
    }
}

