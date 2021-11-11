package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import io.reactivex.Completable
import io.reactivex.Single

interface MessageRepository {

    fun getMessageList(
        numBefore: Int,
        numAfter: Int,
        channelName: String,
        topicName: String
    ): Single<List<Message>>

    fun addMessage(channelName: String, topicName: String, text: String): Completable
}
