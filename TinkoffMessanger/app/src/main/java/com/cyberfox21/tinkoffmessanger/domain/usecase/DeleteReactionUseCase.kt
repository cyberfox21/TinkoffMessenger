package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionRepository
import io.reactivex.Completable

class DeleteReactionUseCase(private val reactionRepository: ReactionRepository) {
    operator fun invoke(messageId: Int, reactionName: String): Completable =
        reactionRepository.deleteReaction(
            messageId = messageId,
            reactionName = reactionName
        )
}
