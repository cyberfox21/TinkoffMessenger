package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionsRepository
import io.reactivex.Completable

class DeleteReactionUseCase(private val reactionRepository: ReactionsRepository) {
    operator fun invoke(messageId: Int, reactionName: String): Completable =
        reactionRepository.deleteReaction(
            messageId = messageId,
            reactionName = reactionName
        )
}
