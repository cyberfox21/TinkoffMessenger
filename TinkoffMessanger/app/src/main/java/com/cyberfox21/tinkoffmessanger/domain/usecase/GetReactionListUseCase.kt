package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetReactionListUseCase @Inject constructor(private val reactionRepository: ReactionsRepository) {
    operator fun invoke(): Observable<List<Reaction>> =
        reactionRepository.getReactionList().toObservable()
}
