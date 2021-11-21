package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import io.reactivex.Completable
import io.reactivex.Flowable

interface ReactionsRepository {

    fun getReactionList(): Flowable<List<Reaction>>

    fun addReaction(messageId: Int, reactionName: String): Completable

    fun deleteReaction(messageId: Int, reactionName: String): Completable
}
