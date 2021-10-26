package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionRepository

class GetReactionListUseCase(private val reactionRepository: ReactionRepository) {
    operator fun invoke(): List<String> = reactionRepository.getReactionList()
}
