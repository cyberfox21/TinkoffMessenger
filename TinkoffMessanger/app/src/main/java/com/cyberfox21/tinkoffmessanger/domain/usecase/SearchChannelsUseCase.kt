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
                    var flag = channel.name.contains(searchQuery, ignoreCase = true)
                    if (!flag) channel.listOfTopics.forEach { topic ->
                        if (topic.title.contains(searchQuery)) {
                            flag = true
                        }
                    }
                    flag
                }
            }
    }
}