package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.enum.LoadType
import io.reactivex.Completable
import io.reactivex.Observable

interface MessagesRepository {

    fun getMessageList(
        channelName: String,
        topicName: String,
        loadType: LoadType,
        lastMessageId: Int = UNDEFINED_LAST_MESSAGE_ID
    ): Observable<Result<List<Message>>>

    fun addMessage(channelName: String, topicName: String, msg: String): Completable

    companion object {
        private const val UNDEFINED_LAST_MESSAGE_ID = -1
    }
}
