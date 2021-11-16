package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionsRepository
import io.reactivex.Single

class GetReactionListUseCase(private val reactionRepository: ReactionsRepository) {
    operator fun invoke(): Single<List<Reaction>> = reactionRepository.getReactionList()
}
