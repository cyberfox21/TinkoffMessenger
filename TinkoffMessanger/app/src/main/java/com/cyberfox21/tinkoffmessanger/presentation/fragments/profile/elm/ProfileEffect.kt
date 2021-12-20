package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

import com.cyberfox21.tinkoffmessanger.domain.enums.UserStatus

sealed class ProfileEffect {
    data class UserLoadError(val error: Throwable) : ProfileEffect()
    object UserEmpty : ProfileEffect()
    data class UserPresenceLoadedSuccess(val status: UserStatus) : ProfileEffect()
}
