package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

import com.cyberfox21.tinkoffmessanger.domain.entity.User

// События разделяются по типу источника, для View это Event.Ui, а для Actor это Event.Internal.

sealed class ProfileEvent {
    sealed class Ui : ProfileEvent() {

        object GetCurrentUser : Ui()

        data class GetSelectedUser(val user: User) : Ui()

    }

    sealed class Internal : ProfileEvent() {

        data class UserLoaded(val user: User?) : Internal()

        data class ErrorLoading(val error: Throwable) : Internal()

    }

}
