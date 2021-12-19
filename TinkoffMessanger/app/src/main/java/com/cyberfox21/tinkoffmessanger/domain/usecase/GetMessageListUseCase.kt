package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.enum.LoadType
import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import javax.inject.Inject

class GetMessageListUseCase @Inject constructor(private val repository: MessagesRepository) {
    operator fun invoke(channelName: String, topicName: String, loadType: LoadType) =
        repository.getMessageList(
            channelName = channelName,
            topicName = topicName,
            loadType = loadType
        )
}
