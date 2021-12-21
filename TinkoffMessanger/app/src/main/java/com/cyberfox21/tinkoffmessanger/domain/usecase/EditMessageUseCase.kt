package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import io.reactivex.Completable
import javax.inject.Inject

class EditMessageUseCase @Inject constructor(private val repository: MessagesRepository) {
    operator fun invoke(msgId: Int, text: String): Completable = repository.editMessage(msgId, text)
}
