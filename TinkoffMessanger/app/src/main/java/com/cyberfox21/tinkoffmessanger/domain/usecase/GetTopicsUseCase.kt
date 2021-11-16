package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.TopicsRepository

class GetTopicsUseCase(private val repository: TopicsRepository) {
    operator fun invoke(channelId: Int) = repository.getTopics(channelId)
}