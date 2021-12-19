package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.Api
import com.cyberfox21.tinkoffmessanger.data.api.Narrow
import com.cyberfox21.tinkoffmessanger.data.api.response.MessagesResponse
import com.cyberfox21.tinkoffmessanger.data.database.dao.MessagesDao
import com.cyberfox21.tinkoffmessanger.data.mapToMessage
import com.cyberfox21.tinkoffmessanger.data.mapToMessageDBModel
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.enum.LoadType
import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ExperimentalSerializationApi
class MessagesRepositoryImpl @Inject constructor(
    private val api: Api,
    private val messagesDao: MessagesDao
) : MessagesRepository {

    private fun getMessagesFromDB(topicName: String): Single<Result<List<Message>>> =
        messagesDao.getMessageList(topicName)
            .map { dbModels ->
                Result.success(dbModels
                    .map { it.mapToMessage() }
                    .sortedBy { msg -> msg.time }
                    .takeLast(DB_LOAD_SIZE)
                )
            }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())


    private fun getMessagesFromNetwork(
        channelName: String,
        topicName: String,
        lastMessageId: Int = UNDEFINED_LAST_MESSAGE_ID
    ): Single<Result<List<Message>>> {
        val narrowList = listOf(
            Narrow(STREAM_KEY, channelName),
            Narrow(TOPIC_KEY, topicName)
        )
        return (if (lastMessageId != UNDEFINED_LAST_MESSAGE_ID)
            getMessagesWithAnchor(lastMessageId, narrowList)
        else getMessagesWithoutAnchor(narrowList))
            .map { response ->
                Result.success(response.messages
                    .map { it.mapToMessage() }
                    .sortedBy { msg -> msg.time }
                    .takeLast(SERVER_LOAD_SIZE)
                )
            }
            .doOnSuccess { result ->
                result.map { message ->
                    messagesDao.addMessageListToDB(message.map { it.mapToMessageDBModel(topicName) }
                        .sortedBy { msg -> msg.time })
                }
            }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())
    }

    private fun getMessagesWithAnchor(
        lastMessageId: Int,
        narrowList: List<Narrow>
    ): Single<MessagesResponse> = api.getMessages(
        messagesNumberBefore = SERVER_LOAD_SIZE,
        messagesNumberAfter = AFTER_MESSAGES_COUNT,
        narrowFilterArray = Json.encodeToString(narrowList),
        anchor = lastMessageId
    )


    private fun getMessagesWithoutAnchor(narrowList: List<Narrow>): Single<MessagesResponse> =
        api.getMessages(
            messagesNumberBefore = SERVER_LOAD_SIZE,
            messagesNumberAfter = AFTER_MESSAGES_COUNT,
            narrowFilterArray = Json.encodeToString(narrowList)
        )

    override fun getMessageList(
        channelName: String,
        topicName: String,
        loadType: LoadType,
        lastMessageId: Int
    ): Observable<Result<List<Message>>> = when (loadType) {

        LoadType.NETWORK -> {
            if (lastMessageId != UNDEFINED_LAST_MESSAGE_ID)
                getMessagesFromNetwork(channelName, topicName, lastMessageId)
                    .toObservable()
                    .subscribeOn(Schedulers.io())
            else getMessagesFromNetwork(channelName, topicName)
                .toObservable()
                .subscribeOn(Schedulers.io())
        }

        LoadType.ANY -> {
            if (lastMessageId != UNDEFINED_LAST_MESSAGE_ID)
                Observable.concat(
                    getMessagesFromNetwork(channelName, topicName, lastMessageId).toObservable(),
                    getMessagesFromDB(topicName).toObservable()
                ).subscribeOn(Schedulers.io())
            else Observable.concat(
                getMessagesFromNetwork(channelName, topicName).toObservable(),
                getMessagesFromDB(topicName).toObservable()
            ).subscribeOn(Schedulers.io())
        }
    }

    override fun addMessage(channelName: String, topicName: String, msg: String)
            : Completable {
        return api.sendMessageToChannel(
            channel = channelName,
            topic = topicName,
            content = msg
        ).subscribeOn(Schedulers.io())
    }

    private companion object {
        private const val UNDEFINED_LAST_MESSAGE_ID = -1

        private const val AFTER_MESSAGES_COUNT = 0

        private const val SERVER_LOAD_SIZE = 20
        private const val DB_LOAD_SIZE = 50

        const val STREAM_KEY = "stream"
        const val TOPIC_KEY = "topic"
    }

}
