package com.cyberfox21.tinkoffmessanger.domain.repository

import androidx.lifecycle.LiveData
import com.cyberfox21.tinkoffmessanger.domain.entity.Message

interface MessageRepository {

    fun getMessageList(): LiveData<List<Message>>

    fun getMessage(msgId: Int): Message?

    fun addMessage(msg: Message)

    fun deleteMessage(msg: Message)

    fun addEmojiToMessage(msg: Message)
}
