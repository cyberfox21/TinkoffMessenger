package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile

import com.cyberfox21.tinkoffmessanger.domain.entity.User

sealed class ProfileScreenState {

    class Result(val user: User) : ProfileScreenState()

    object Loading : ProfileScreenState()

    class Error(val error: Throwable) : ProfileScreenState()

}