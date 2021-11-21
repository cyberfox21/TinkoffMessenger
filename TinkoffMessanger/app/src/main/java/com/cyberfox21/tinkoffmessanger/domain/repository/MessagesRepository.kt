package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import io.reactivex.Completable
import io.reactivex.Flowable

interface MessagesRepository {

    fun getMessageList(
        numBefore: Int,
        numAfter: Int,
        channelName: String,
        topicName: String
    ): Flowable<List<Message>>

    fun addMessage(channelName: String, topicName: String, text: String): Completable
}
