package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import io.reactivex.Completable
import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(private val repository: MessagesRepository) {
    operator fun invoke(msgId: Int): Completable = repository.deleteMessage(msgId)
}
