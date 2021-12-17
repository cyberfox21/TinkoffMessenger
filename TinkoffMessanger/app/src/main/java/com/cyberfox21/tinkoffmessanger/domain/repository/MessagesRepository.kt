package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.enum.LoadType
import io.reactivex.Completable
import io.reactivex.Observable

interface MessagesRepository {

    fun getMessageList(
        numBefore: Int,
        numAfter: Int,
        channelName: String,
        topicName: String,
        loadType: LoadType
    ): Observable<List<Message>>

    fun addMessage(channelName: String, topicName: String, msg: String): Completable
}
