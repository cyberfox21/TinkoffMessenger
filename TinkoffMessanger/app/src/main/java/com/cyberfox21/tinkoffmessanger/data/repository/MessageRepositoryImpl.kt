package com.cyberfox21.tinkoffmessanger.data.repository

import android.app.Application
import com.cyberfox21.tinkoffmessanger.data.api.ApiFactory
import com.cyberfox21.tinkoffmessanger.data.api.Narrow
import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase
import com.cyberfox21.tinkoffmessanger.data.mapToMessage
import com.cyberfox21.tinkoffmessanger.data.mapToMessageDBModel
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MessageRepositoryImpl(application: Application) : MessagesRepository {

    private val api = ApiFactory.api

    private val messagesDao =
        AppDatabase.getInstance(application, AppDatabase.MESSAGES_DB_NAME).messagesDao()

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
    ): Flowable<List<Message>> {
        return getMessagesFromDB(topicName).toFlowable().switchIfEmpty(
            getMessagesFromNetwork(numBefore, numAfter, channelName, topicName).toFlowable()
        )

    }


    override fun addMessage(channelName: String, topicName: String, text: String): Completable {
        return api.sendMessageToChannel(
            channel = channelName,
            topic = topicName,
            content = text
        ).subscribeOn(Schedulers.io())
    }

}
