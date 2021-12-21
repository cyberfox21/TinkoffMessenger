package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import io.reactivex.Completable
import javax.inject.Inject

class ChangeTopicUseCase @Inject constructor(private val repository: MessagesRepository) {
    operator fun invoke(msgId: Int, topic: String): Completable =
        repository.changeMessageTopic(msgId, topic)
}
