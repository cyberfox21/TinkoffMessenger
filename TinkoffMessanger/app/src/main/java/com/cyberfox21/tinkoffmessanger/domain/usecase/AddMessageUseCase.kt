package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import io.reactivex.Completable
import javax.inject.Inject

class AddMessageUseCase @Inject constructor(private val repository: MessagesRepository) {
    operator fun invoke(channelName: String, topicName: String, msg: String): Completable =
        repository.addMessage(
            channelName = channelName,
            topicName = topicName,
            msg = msg
        )
}
