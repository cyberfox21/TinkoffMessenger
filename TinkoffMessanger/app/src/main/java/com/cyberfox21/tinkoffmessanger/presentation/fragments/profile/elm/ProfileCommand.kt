package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

sealed class ProfileCommand {
    object LoadCurrentUser : ProfileCommand()
    data class GetUserPresence(val userId: Int) : ProfileCommand()
}
