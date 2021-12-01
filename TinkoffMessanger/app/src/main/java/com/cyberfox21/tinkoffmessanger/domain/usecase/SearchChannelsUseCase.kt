package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.repository.ChannelsRepository
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.Category
import io.reactivex.Observable
import javax.inject.Inject

class SearchChannelsUseCase @Inject constructor(private val repository: ChannelsRepository) {
    operator fun invoke(searchQuery: String, category: Category): Observable<List<Channel>> {
        val channels = repository.searchChannels(searchQuery, category)
            .map { channels ->
                channels.filter { channel ->
                    return@filter channel.name.contains(searchQuery, ignoreCase = true)
                }
            }
        return channels
    }
}