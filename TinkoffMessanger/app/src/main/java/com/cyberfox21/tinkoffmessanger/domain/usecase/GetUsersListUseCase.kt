package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.UsersRepository

class GetUsersListUseCase(private val repository: UsersRepository) {
    operator fun invoke() = repository.getUsersList()
}
