package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

// команды для ui
sealed class ProfileEffect {
    data class UserLoadError(val error: Throwable) : ProfileEffect()
    object UserEmpty : ProfileEffect()
}
