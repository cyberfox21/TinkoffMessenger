package com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class PeopleReducer : DslReducer<PeopleEvent, PeopleState, PeopleEffect, PeopleCommand>() {
    override fun Result.reduce(event: PeopleEvent): Any {
        return when (event) {
            is PeopleEvent.Internal.UserListLoaded -> {
                if (event.users?.isEmpty() == true) {
                    state {
                        copy(
                            users = listOf(),
                            isEmptyState = true,
                            isLoading = false,
                            error = null
                        )
                    }
                    effects { PeopleEffect.UsersListEmpty }
                } else {
                    state {
                        copy(
                            users = event.users,
                            isEmptyState = false,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
            is PeopleEvent.Internal.UserListLoadError -> {
                state {
                    copy(
                        users = null,
                        isEmptyState = false,
                        isLoading = false,
                        error = event.error
                    )
                }
                effects { PeopleEffect.UserListLoadError(event.error) }
            }
            is PeopleEvent.Ui.GetUserList -> {
                state {
                    copy(
                        users = null,
                        isEmptyState = false,
                        isLoading = true,
                        error = null
                    )
                }
                commands { +PeopleCommand.LoadUserList }
            }
        }
    }
}