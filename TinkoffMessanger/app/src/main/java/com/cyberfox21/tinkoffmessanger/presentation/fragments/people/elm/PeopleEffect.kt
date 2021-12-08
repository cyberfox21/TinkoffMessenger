package com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm

sealed class PeopleEffect {
    data class UserListLoadError(val error: Throwable) : PeopleEffect()
    object UsersListEmpty : PeopleEffect()
}
