package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.elm

import android.content.Context
import com.cyberfox21.tinkoffmessanger.data.repository.ChannelsRepositoryImpl
import com.cyberfox21.tinkoffmessanger.data.repository.TopicsRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetTopicsUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.SearchChannelsUseCase
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class ChannelsActor(context: Context) : ActorCompat<ChannelsCommand, ChannelsEvent> {

    private val channelsRepository = ChannelsRepositoryImpl(context)
    private val topicsRepository = TopicsRepositoryImpl(context)

    private val searchChannelsUseCase = SearchChannelsUseCase(channelsRepository)

    private val getTopicsUseCase = GetTopicsUseCase(topicsRepository)

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