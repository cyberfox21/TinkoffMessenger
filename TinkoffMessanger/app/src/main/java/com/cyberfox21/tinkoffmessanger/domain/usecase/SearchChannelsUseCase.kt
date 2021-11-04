package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.enums.Category
import com.cyberfox21.tinkoffmessanger.domain.repository.ChannelsRepository
import io.reactivex.Observable

class SearchChannelsUseCase(private val repository: ChannelsRepository) {
    operator fun invoke(searchQuery: String, category: Category): Observable<List<Channel>> {
        return repository.searchChannels(searchQuery, category)
            .map { channels ->
                channels.filter { channel ->
                    if (channel.name.contains(searchQuery, ignoreCase = true)) {
                        return@filter true
                    }
                    channel.listOfTopics
                        .find { topic -> topic.title.contains(searchQuery) }
                        ?.let { true } ?: false
                }
            }
    }
}