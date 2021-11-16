package com.cyberfox21.tinkoffmessanger.data.repository

import android.util.Log
import androidx.core.text.HtmlCompat
import com.cyberfox21.tinkoffmessanger.data.api.ApiFactory
import com.cyberfox21.tinkoffmessanger.data.api.Narrow
import com.cyberfox21.tinkoffmessanger.domain.entity.Message
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.repository.MessagesRepository
import com.cyberfox21.tinkoffmessanger.util.DateFormatter
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object MessageRepositoryImpl : MessagesRepository {

    private val api = ApiFactory.api

    override fun getMessageList(
        numBefore: Int,
        numAfter: Int,
        channelName: String,
        topicName: String
    ): Single<List<Message>> {
        val narrowList = listOf(
            Narrow("stream", channelName),
            Narrow("topic", topicName)
        )
        Log.d("CHECKER", Json.encodeToString(narrowList))
        return api.getMessages(
            messagesNumberBefore = numBefore,
            messagesNumberAfter = numAfter,
            narrowFilterArray = Json.encodeToString(narrowList)
        ).flatMap { response ->
            val mappedMessageList = response.messages.map {
                Message(
                    id = it.id,
                    message = HtmlCompat.fromHtml(it.content, 0).toString(),
                    time = DateFormatter.utcToDate(it.timestamp),
                    senderId = it.senderId,
                    senderName = it.senderFullName,
                    senderAvatarUrl = it.avatarUrl,
                    isCurrentUser = it.isMeMessage,
                    reactions = it.reactions.map { reactionDTO ->
                        Reaction(
                            reaction = reactionDTO.code,
                            name = reactionDTO.name,
                            userId = reactionDTO.userId
                        )
                    }
                )
            }
            Single.just(mappedMessageList)
        }.subscribeOn(Schedulers.io())
    }

    override fun addMessage(channelName: String, topicName: String, text: String): Completable {
        return api.sendMessageToChannel(
            channel = channelName,
            topic = topicName,
            content = text
        ).subscribeOn(Schedulers.io())
    }

}
