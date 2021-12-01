package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import javax.inject.Inject

class AddMessageUseCase @Inject constructor(private val repository: MessagesRepository) {
    operator fun invoke(channelName: String, topicName: String, msg: Message) =
        repository.addMessage(
            channelName = channelName,
            topicName = topicName,
            msg = msg
        )
}
