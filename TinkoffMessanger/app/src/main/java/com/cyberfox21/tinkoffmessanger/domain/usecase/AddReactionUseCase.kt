package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionRepository
import io.reactivex.Completable

class AddReactionUseCase(private val reactionRepository: ReactionRepository) {
    operator fun invoke(messageId: Int, reactionName: String): Completable =
        reactionRepository.addReaction(
            messageId = messageId,
            reactionName = reactionName
        )
}
