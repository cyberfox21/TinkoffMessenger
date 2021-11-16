package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import io.reactivex.Single

class GetMessageListUseCase(private val repository: MessagesRepository) {
    operator fun invoke(
        numBefore: Int,
        numAfter: Int,
        channelName: String,
        topicName: String
    ): Single<List<Message>> =
        repository.getMessageList(
            numBefore = numBefore,
            numAfter = numAfter,
            channelName = channelName,
            topicName = topicName
        )
}
