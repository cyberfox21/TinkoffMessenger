package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.MessageRepository

class AddMessageUseCase(private val repository: MessageRepository) {
    operator fun invoke(channelName: String, topicName: String, text: String) =
        repository.addMessage(
            channelName = channelName,
            topicName = topicName,
            text = text
        )
}
