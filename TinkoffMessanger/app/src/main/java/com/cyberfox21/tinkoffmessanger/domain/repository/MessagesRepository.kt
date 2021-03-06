package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.enums.LoadType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface MessagesRepository {

    fun getMessageList(
        channelName: String,
        topicName: String,
        loadType: LoadType,
        lastMessageId: Int = UNDEFINED_LAST_MESSAGE_ID
    ): Observable<Result<List<Message>>>

    fun getMessageFromServer(
        channelName: String,
        topicName: String,
        messageId: Int = UNDEFINED_LAST_MESSAGE_ID
    ): Single<Result<Message>>

    fun addMessage(channelName: String, topicName: String, msg: String): Completable

    fun deleteMessage(msgId: Int): Completable

    fun editMessage(msgId: Int, text: String): Completable

    fun changeMessageTopic(msgId: Int, topic: String): Completable

    companion object {
        private const val UNDEFINED_LAST_MESSAGE_ID = -1
    }
}
