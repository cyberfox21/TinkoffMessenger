package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import com.cyberfox21.tinkoffmessanger.domain.usecase.GetTopicsUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.SearchChannelsUseCase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vivid.money.elmslie.core.ActorCompat

class ChannelsActor(
    private val searchChannelsUseCase: SearchChannelsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase
) : ActorCompat<ChannelsCommand, ChannelsEvent> {

    override fun execute(command: ChannelsCommand): Observable<ChannelsEvent> {
        return when (command) {
            is ChannelsCommand.GetChannelsList -> {
                searchChannelsUseCase(command.searchQuery, command.category)
                    .subscribeOn(Schedulers.io())
                    .map { result ->
                        result.fold(
                            { list ->
                                if (list.isEmpty()) {
                                    ChannelsEvent.Internal.ChannelsLoadedEmpty
                                } else ChannelsEvent.Internal.ChannelsLoaded(list)
                            },
                            { ChannelsEvent.Internal.ChannelsLoadError(it) }
                        )
                    }
            }
            is ChannelsCommand.GetTopicsList -> {
                getTopicsUseCase(command.channelId)
                    .mapEvents(
                        { topics ->
                            ChannelsEvent.Internal.TopicsLoaded(
                                topics,
                                command.channelId,
                                command.channelName,
                                command.isSelected
                            )
                        },
                        { error -> ChannelsEvent.Internal.TopicsLoadError(error) }
                    ).subscribeOn(Schedulers.io())
            }
        }
    }
}
