package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.database.dao.MessagesDao
import com.cyberfox21.tinkoffmessanger.data.mapToMessage
import com.cyberfox21.tinkoffmessanger.data.mapToMessageDBModel
import com.cyberfox21.tinkoffmessanger.data.network.Narrow
import com.cyberfox21.tinkoffmessanger.data.network.api.MessagesApi
import com.cyberfox21.tinkoffmessanger.data.network.response.MessagesResponse
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.enums.LoadType
import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Named

@ExperimentalSerializationApi
class MessagesRepositoryImpl @Inject constructor(
    @Named("MessagesApi") private val api: MessagesApi,
    private val messagesDao: MessagesDao
) : MessagesRepository {

    private fun getMessagesFromDB(topicName: String): Single<Result<List<Message>>> =
        messagesDao.getMessageList(topicName)
            .map { dbModels ->
                Result.success(dbModels
                    .map { it.mapToMessage() }
                    .sortedBy { msg -> msg.time }
                    .takeLast(DB_LOAD_SIZE)
                    .reversed()
                )
            }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())


    private fun getMessagesFromNetwork(
        channelName: String,
        topicName: String,
        lastMessageId: Int
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
                    .reversed()
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
        anchor = lastMessageId,
        messagesNumberBefore = SERVER_LOAD_SIZE,
        messagesNumberAfter = AFTER_MESSAGES_COUNT,
        narrowFilterArray = Json.encodeToString(narrowList)
    )

    private fun getMessagesWithoutAnchor(narrowList: List<Narrow>): Single<MessagesResponse> =
        api.getMessages(
            messagesNumberBefore = SERVER_LOAD_SIZE,
            messagesNumberAfter = AFTER_MESSAGES_COUNT,
            narrowFilterArray = Json.encodeToString(narrowList)
        )

    override fun getMessageFromServer(
        channelName: String,
        topicName: String,
        messageId: Int
    ): Single<Result<Message>> {
        val narrowList: List<Narrow> = listOf(
            Narrow(STREAM_KEY, channelName), Narrow(TOPIC_KEY, topicName)
        )
        return api.getMessages(
            anchor = messageId,
            messagesNumberBefore = AFTER_MESSAGES_COUNT,
            messagesNumberAfter = AFTER_MESSAGES_COUNT,
            narrowFilterArray = Json.encodeToString(narrowList)
        ).map { response -> Result.success(response.messages.first().mapToMessage()) }
            .onErrorReturn { error -> Result.failure(error) }
            .doOnSuccess { result ->
                if (result.isSuccess) {
                    val message = result.getOrNull()
                    if (message != null) messagesDao.addMessageListToDB(
                        listOf(message.mapToMessageDBModel(topicName))
                    )
                }
            }.subscribeOn(Schedulers.io())
    }

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
            else getMessagesFromNetwork(channelName, topicName, UNDEFINED_LAST_MESSAGE_ID)
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
                getMessagesFromNetwork(
                    channelName,
                    topicName,
                    UNDEFINED_LAST_MESSAGE_ID
                ).toObservable(),
                getMessagesFromDB(topicName).toObservable()
            ).subscribeOn(Schedulers.io())
        }
    }

    override fun addMessage(channelName: String, topicName: String, msg: String): Completable {
        return api.sendMessage(
            channel = channelName,
            topic = topicName,
            content = msg
        ).subscribeOn(Schedulers.io())
    }

    override fun deleteMessage(msgId: Int): Completable =
        api.deleteMessage(msgId).subscribeOn(Schedulers.io())

    override fun editMessage(msgId: Int, text: String): Completable =
        api.editMessage(msgId, text).subscribeOn(Schedulers.io())

    override fun changeMessageTopic(msgId: Int, topic: String): Completable =
        api.changeMessageTopic(msgId, topic).subscribeOn(Schedulers.io())

    private companion object {
        private const val UNDEFINED_LAST_MESSAGE_ID = -1

        private const val AFTER_MESSAGES_COUNT = 0

        private const val SERVER_LOAD_SIZE = 20
        private const val DB_LOAD_SIZE = 50

        const val STREAM_KEY = "stream"
        const val TOPIC_KEY = "topic"
    }

}
