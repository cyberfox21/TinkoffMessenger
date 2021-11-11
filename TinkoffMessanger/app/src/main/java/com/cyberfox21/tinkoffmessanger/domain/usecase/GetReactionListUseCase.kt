package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionRepository
import io.reactivex.Single

class GetReactionListUseCase(private val reactionRepository: ReactionRepository) {
    operator fun invoke(): Single<List<Reaction>> = reactionRepository.getReactionList()
}
