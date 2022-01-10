package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.database.dao.ReactionListDao
import com.cyberfox21.tinkoffmessanger.data.mapToReactionForReactionList
import com.cyberfox21.tinkoffmessanger.data.mapToReactionListDBModel
import com.cyberfox21.tinkoffmessanger.data.network.api.ReactionsApi
import com.cyberfox21.tinkoffmessanger.di.qualifier.ReactionsApiQualifier
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionsRepository
import com.cyberfox21.tinkoffmessanger.presentation.util.EmojiFormatter
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReactionsRepositoryImpl @Inject constructor(
    @ReactionsApiQualifier private val api: ReactionsApi,
    private val reactionListDao: ReactionListDao
) : ReactionsRepository {

    private fun getReactionListFromDB(): Single<List<Reaction>> =
        reactionListDao.getReactionList().map { list ->
            list.map {
                it.mapToReactionForReactionList()
            }
        }

    private fun getReactionListFromNetwork() = api.getReactions()
        .map {
            val jObject = it.reactionsObject
            EmojiFormatter.jsonObjectToReactionsList(jObject)
        }
        .doOnSuccess { reactionList ->
            reactionListDao.addReactionListToDB(reactionList.map {
                it.mapToReactionListDBModel()
            })
        }
        .subscribeOn(Schedulers.io())

    override fun getReactionList(): Flowable<List<Reaction>> =
        Single.concat(
            getReactionListFromDB(),
            getReactionListFromNetwork()
        )

    override fun addReaction(messageId: Int, reactionName: String): Completable {
        return api.addReaction(
            messageId = messageId,
            emojiName = reactionName
        ).subscribeOn(Schedulers.io())
    }

    override fun deleteReaction(
        messageId: Int,
        reactionName: String,
        emojiCode: String,
        emojiType: String
    ): Completable {
        return api.deleteReaction(
            messageId = messageId,
            emojiName = reactionName,
            emojiCode = emojiCode,
            emojiType = emojiType
        ).subscribeOn(Schedulers.io())
    }

}
