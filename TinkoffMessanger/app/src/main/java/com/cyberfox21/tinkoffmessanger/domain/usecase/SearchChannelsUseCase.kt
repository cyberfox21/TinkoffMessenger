package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.repository.ChannelsRepository
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.enums.Category
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchChannelsUseCase @Inject constructor(private val repository: ChannelsRepository) {
    operator fun invoke(
        searchQuery: String,
        category: Category
    ): Observable<Result<List<Channel>>> {
        val channels: Observable<Result<List<Channel>>> =
            repository.searchChannels(searchQuery, category)
                .debounce(500L, TimeUnit.MILLISECONDS, Schedulers.io())
                .distinctUntilChanged()
                .map { result ->
                    result.fold(
                        onSuccess = {
                            result.map { channels ->
                                channels.filter { channel ->
                                    return@filter channel.name.contains(
                                        searchQuery,
                                        ignoreCase = true
                                    )
                                }
                            }
                        },
                        onFailure = { Result.failure(it) }
                    )
                }
        return channels
    }
}
