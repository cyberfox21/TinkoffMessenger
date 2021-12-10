package com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm

sealed class PeopleCommand {
    data class LoadUserList(val query: String) : PeopleCommand()
}
