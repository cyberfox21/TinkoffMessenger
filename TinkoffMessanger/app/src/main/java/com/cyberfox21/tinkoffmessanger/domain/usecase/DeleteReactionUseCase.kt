package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionsRepository
import io.reactivex.Completable
import javax.inject.Inject

class DeleteReactionUseCase @Inject constructor(private val reactionRepository: ReactionsRepository) {
    operator fun invoke(messageId: Int, reaction: Reaction): Completable =
        reactionRepository.deleteReaction(
            messageId = messageId,
            reactionName = reaction.name,
            emojiCode = reaction.code,
            emojiType = reaction.type
        )
}
