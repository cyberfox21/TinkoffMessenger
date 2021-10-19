package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.repository.MessageRepository

class AddMessageUseCase(private val repository: MessageRepository) {
    operator fun invoke(msg: Message) {
        repository.addMessage(msg)
    }
}