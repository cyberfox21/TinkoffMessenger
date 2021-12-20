package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(private val repository: MessagesRepository) {
    operator fun invoke(
        channelName: String,
        topicName: String,
        messageId: Int
    ) =
        repository.getMessageFromServer(
            channelName = channelName,
            topicName = topicName,
            messageId = messageId
        )
}
