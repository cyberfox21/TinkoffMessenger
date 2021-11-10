package com.cyberfox21.tinkoffmessanger.presentation.fragments.people

import com.cyberfox21.tinkoffmessanger.domain.entity.User

sealed class UsersScreenState {
    class Result(val items: List<User>) : UsersScreenState()

    object Loading : UsersScreenState()

    class Error(val error: Throwable) : UsersScreenState()
}