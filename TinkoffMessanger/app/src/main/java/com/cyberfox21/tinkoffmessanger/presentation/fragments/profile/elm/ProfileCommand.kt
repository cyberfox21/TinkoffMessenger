package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

// команды запуска операций в Actor
sealed class ProfileCommand {
    object LoadCurrentUser : ProfileCommand()
}
