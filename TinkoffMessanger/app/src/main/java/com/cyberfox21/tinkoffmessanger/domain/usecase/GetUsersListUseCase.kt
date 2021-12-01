package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.UsersRepository
import javax.inject.Inject

class GetUsersListUseCase @Inject constructor(private val repository: UsersRepository) {
    operator fun invoke() = repository.getUsersList()
}
