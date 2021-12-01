package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import javax.inject.Inject

class GetMessageListUseCase @Inject constructor(private val repository: MessagesRepository) {
    operator fun invoke(
        numBefore: Int,
        numAfter: Int,
        channelName: String,
        topicName: String
    ) =
        repository.getMessageList(
            numBefore = numBefore,
            numAfter = numAfter,
            channelName = channelName,
            topicName = topicName
        )
}
