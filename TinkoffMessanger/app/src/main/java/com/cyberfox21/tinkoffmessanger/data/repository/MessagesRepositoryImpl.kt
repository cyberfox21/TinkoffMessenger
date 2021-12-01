package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.Api
import com.cyberfox21.tinkoffmessanger.data.api.Narrow
import com.cyberfox21.tinkoffmessanger.data.database.dao.MessagesDao
import com.cyberfox21.tinkoffmessanger.data.mapToMessage
import com.cyberfox21.tinkoffmessanger.data.mapToMessageDBModel
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val api: Api,
    private val messagesDao: MessagesDao
) : MessagesRepository {

    private fun getMessagesFromDB(topicName: String) =
        messagesDao.getMessageList(topicName).map { dbModels ->
            dbModels.map {
                it.mapToMessage()
            }
        }.subscribeOn(Schedulers.io())

    private fun getMessagesFromNetwork(
        numBefore: Int,
        numAfter: Int,
        channelName: String,
        topicName: String
    ): Single<List<Message>> {
        // todo check internet is available
        val narrowList = listOf(
            Narrow("stream", channelName),
            Narrow("topic", topicName)
        )
        return api.getMessages(
            messagesNumberBefore = numBefore,
            messagesNumberAfter = numAfter,
            narrowFilterArray = Json.encodeToString(narrowList)
        ).map { response ->
            response.messages.map {
                it.mapToMessage()
            }
        }.doOnSuccess {
            messagesDao.addMessageListToDB(it.map { message ->
                message.mapToMessageDBModel(
                    topicName
                )
            })
        }.subscribeOn(Schedulers.io())
    }

    override fun getMessageList(
        numBefore: Int,
        numAfter: Int,
        channelName: String,
        topicName: String
    ): Observable<List<Message>> {
        return Observable.concat(
            getMessagesFromDB(topicName).toObservable().map { it.sortedBy { msg -> msg.time } },
            getMessagesFromNetwork(numBefore, numAfter, channelName, topicName).toObservable()
                .map { it.sortedBy { msg -> msg.time } }
        ).subscribeOn(Schedulers.io())
    }

    override fun addMessage(channelName: String, topicName: String, msg: Message): Completable {
        return api.sendMessageToChannel(
            channel = channelName,
            topic = topicName,
            content = msg.message
        ).subscribeOn(Schedulers.io())
    }

}
