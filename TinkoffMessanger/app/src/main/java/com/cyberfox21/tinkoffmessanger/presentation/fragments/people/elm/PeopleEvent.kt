package com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.User

sealed class PeopleEvent {
    sealed class Ui : PeopleEvent() {
        data class GetUserList(val query: String) : Ui()
    }

    sealed class Internal : PeopleEvent() {
        data class UserListLoaded(val users: List<User>) : Internal()
        object UserListLoadEmpty : Internal()
        data class UserListLoadError(val error: Throwable) : Internal()
    }
}
