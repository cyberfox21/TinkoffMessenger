package com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.User

sealed class PeopleEvent {
    sealed class Ui : PeopleEvent() {
        object GetUserList : Ui()
    }

    sealed class Internal : PeopleEvent() {
        data class UserListLoaded(val users: List<User>?) : Internal()

        data class UserListLoadError(val error: Throwable) : Internal()
    }
}
