package com.cyberfox21.tinkoffmessanger.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.repository.MessageRepository

object MessageRepositoryImpl : MessageRepository {

    private var _messageListLD = MutableLiveData<List<Message>>()
    private val messageListLD: LiveData<List<Message>>
        get() = _messageListLD

    private val messageList = mutableListOf<Message>()

    override fun getMessageList(): LiveData<List<Message>> {
        return messageListLD
    }

    override fun addMessage(msg: Message) {
        messageList.add(msg)
        updateList()
    }

    private fun updateList() {
        _messageListLD.value = messageList.toList()
    }

}
