package com.cyberfox21.tinkoffmessanger.domain.usecase

import androidx.lifecycle.LiveData
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.repository.MessageRepository

class GetMessageListUseCase(private val repository: MessageRepository) {
    operator fun invoke(): LiveData<List<Message>> {
        return repository.getMessageList()
    }
}
