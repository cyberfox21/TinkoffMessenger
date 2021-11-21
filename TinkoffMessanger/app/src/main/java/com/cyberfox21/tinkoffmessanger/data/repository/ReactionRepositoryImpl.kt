package com.cyberfox21.tinkoffmessanger.data.repository

import android.app.Application
import com.cyberfox21.tinkoffmessanger.data.api.ApiFactory
import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase
import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase.Companion.REACTION_LIST_DB_NAME
import com.cyberfox21.tinkoffmessanger.data.mapToReactionForReactionList
import com.cyberfox21.tinkoffmessanger.data.mapToReactionListDBModel
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionsRepository
import com.cyberfox21.tinkoffmessanger.util.EmojiFormatter
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ReactionRepositoryImpl(application: Application) : ReactionsRepository {

    private val api = ApiFactory.api

    private val reactionListDao =
        AppDatabase.getInstance(application, REACTION_LIST_DB_NAME).reactionListDao()

    private fun getReactionListFromDB(): Single<List<Reaction>> =
        reactionListDao.getReactionList().map { list ->
            list.map {
                it.mapToReactionForReactionList()
            }
        }

    // todo check is internet available
    private fun getReactionListFromNetwork() = api.getReactions()
        .map {
            val jObject = it.reactionsObject
            EmojiFormatter.jsonObjectToReactionsList(jObject)
        }.doOnSuccess { reactionList ->
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

    override fun deleteReaction(messageId: Int, reactionName: String): Completable {
        return api.deleteReaction(
            messageId = messageId,
            emojiName = reactionName
        ).subscribeOn(Schedulers.io())
    }

}
