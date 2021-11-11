package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import io.reactivex.Completable
import io.reactivex.Single

interface ReactionRepository {

    fun getReactionList(): Single<List<Reaction>>

    fun addReaction(messageId: Int, reactionName: String): Completable

    fun deleteReaction(messageId: Int, reactionName: String): Completable
}
