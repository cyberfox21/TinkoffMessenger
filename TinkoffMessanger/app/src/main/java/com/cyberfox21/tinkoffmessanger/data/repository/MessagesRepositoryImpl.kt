package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.Api
import com.cyberfox21.tinkoffmessanger.data.api.Narrow
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
                Result.success(dbModels.map { it.mapToMessage() }.sortedBy { msg -> msg.time })
            }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())

    private fun getMessagesFromNetwork(
        numBefore: Int,
        numAfter: Int,
        channelName: String,
        topicName: String
    ): Single<Result<List<Message>>> {
        val narrowList = listOf(
            Narrow(STREAM_KEY, channelName),
            Narrow(TOPIC_KEY, topicName)
        )
        return api.getMessages(
            messagesNumberBefore = numBefore,
            messagesNumberAfter = numAfter,
            narrowFilterArray = Json.encodeToString(narrowList)
        )
            .map { response ->
                Result.success(response.messages.map { it.mapToMessage() }
                    .sortedBy { msg -> msg.time })
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

    override fun getMessageList(
        numBefore: Int,
        numAfter: Int,
        channelName: String,
        topicName: String,
        loadType: LoadType
    ): Observable<Result<List<Message>>> = when (loadType) {

        LoadType.NETWORK -> getMessagesFromNetwork(numBefore, numAfter, channelName, topicName)
            .toObservable()
            .subscribeOn(Schedulers.io())

        LoadType.ANY -> {
            Observable.concat(
                getMessagesFromNetwork(numBefore, numAfter, channelName, topicName).toObservable(),
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
        const val STREAM_KEY = "stream"
        const val TOPIC_KEY = "topic"

        private const val CACHE_LOAD_SIZE = 50
        private const val SERVER_LOAD_SIZE = 20
    }

}
