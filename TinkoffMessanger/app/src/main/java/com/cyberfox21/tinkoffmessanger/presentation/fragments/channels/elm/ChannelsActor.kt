package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import com.cyberfox21.tinkoffmessanger.domain.usecase.GetTopicsUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.SearchChannelsUseCase
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class ChannelsActor(
    private val searchChannelsUseCase: SearchChannelsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase
) : ActorCompat<ChannelsCommand, ChannelsEvent> {

    override fun execute(command: ChannelsCommand): Observable<ChannelsEvent> {
        return when (command) {
            is ChannelsCommand.GetChannelsList -> {
                searchChannelsUseCase(command.searchQuery, command.category)
                    .mapEvents(
                        { channels -> ChannelsEvent.Internal.ChannelsLoaded(channels) },
                        { error -> ChannelsEvent.Internal.ChannelsLoadError(error) }
                    )
            }
            is ChannelsCommand.GetTopicsList -> {
                getTopicsUseCase(command.channelId)
                    .toObservable()
                    .mapEvents(
                        { topics ->
                            ChannelsEvent.Internal.TopicsLoaded(
                                topics,
                                command.channelId,
                                command.isSelected
                            )
                        },
                        { error -> ChannelsEvent.Internal.TopicsLoadError(error) }
                    )
            }
        }
    }
}
