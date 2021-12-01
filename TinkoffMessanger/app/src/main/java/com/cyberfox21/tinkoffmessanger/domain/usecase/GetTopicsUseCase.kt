package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.TopicsRepository
import javax.inject.Inject

class GetTopicsUseCase @Inject constructor(private val repository: TopicsRepository) {
    operator fun invoke(channelId: Int) = repository.getTopics(channelId)
}