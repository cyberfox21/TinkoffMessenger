package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.UsersRepository
import javax.inject.Inject

class GetUserPresenceUseCase @Inject constructor(private val repository: UsersRepository) {
    operator fun invoke(id: Int) = repository.getUserPresence(id)
}
