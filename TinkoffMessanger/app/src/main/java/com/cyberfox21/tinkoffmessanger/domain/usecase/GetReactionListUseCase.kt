package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionsRepository

class GetReactionListUseCase(private val reactionRepository: ReactionsRepository) {
    operator fun invoke() = reactionRepository.getReactionList().toObservable()
}
