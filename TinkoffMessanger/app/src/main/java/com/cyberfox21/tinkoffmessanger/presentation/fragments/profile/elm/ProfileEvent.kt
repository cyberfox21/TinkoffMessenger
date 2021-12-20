package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.domain.enums.UserStatus

sealed class ProfileEvent {
    sealed class Ui : ProfileEvent() {

        object GetCurrentUser : Ui()

        data class GetSelectedUser(val user: User) : Ui()

    }

    sealed class Internal : ProfileEvent() {

        data class UserLoaded(val user: User?) : Internal()

        data class ErrorLoading(val error: Throwable) : Internal()

        data class UserPresenceLoaded(val status: UserStatus) : Internal()
        data class UserPresenceLoadedError(val error: Throwable) : Internal()

    }

}
