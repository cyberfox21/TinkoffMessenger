package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.repository.ChannelsRepository
import com.cyberfox21.tinkoffmessanger.presentation.enums.Category

class GetChannelsListUseCase(private val repository: ChannelsRepository) {
    operator fun invoke(category: Category): List<Channel> =
        repository.getChannelsList(category)
}
