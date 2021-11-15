package com.cyberfox21.tinkoffmessanger.domain.usecase

import com.cyberfox21.tinkoffmessanger.domain.repository.UsersRepository

class GetUserUseCase(private val repository: UsersRepository) {
    operator fun invoke(id: Int) = repository.getUser(id)
}