package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.ApiFactory
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionsRepository
import com.cyberfox21.tinkoffmessanger.util.EmojiFormatter
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

object ReactionRepositoryImpl : ReactionsRepository {

    private val api = ApiFactory.api

    private const val UNDEFINED_ID = -1

    var reactions = mutableListOf<Reaction>()

    init {
        for (i in 0 until 50) {
            val unicode = 0x1F600 + i
            reactions.add(
                Reaction(
                    userId = UNDEFINED_ID,
                    name = "Emoji",
                    reaction = String(Character.toChars(unicode))
                )
            )
        }
    }

    override fun getReactionList(): Single<List<Reaction>> {
        val single = api.getReactions()
            .map {
                val jObject = it.reactionsObject
                EmojiFormatter.jsonObjectToReactionsList(jObject)
            }.subscribeOn(Schedulers.io())
        return single
    }

    override fun addReaction(messageId: Int, reactionName: String): Completable {
        return api.addReaction(
            messageId = messageId,
            emojiName = reactionName
        ).subscribeOn(Schedulers.io())
    }

    override fun deleteReaction(messageId: Int, reactionName: String): Completable {
        return api.deleteReaction(
            messageId = messageId,
            emojiName = reactionName
        ).subscribeOn(Schedulers.io())
    }

}
