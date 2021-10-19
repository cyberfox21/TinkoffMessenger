package com.cyberfox21.tinkoffmessanger.domain.repository

import androidx.lifecycle.LiveData
import com.cyberfox21.tinkoffmessanger.domain.entity.Message

interface MessageRepository {

    fun getMessageList(): LiveData<List<Message>>

    fun addMessage(msg: Message)
}