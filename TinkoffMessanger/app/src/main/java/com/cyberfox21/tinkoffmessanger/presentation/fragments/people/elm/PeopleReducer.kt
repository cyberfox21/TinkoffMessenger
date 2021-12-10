package com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm

import android.util.Log
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class PeopleReducer : DslReducer<PeopleEvent, PeopleState, PeopleEffect, PeopleCommand>() {
    override fun Result.reduce(event: PeopleEvent): Any {
        return when (event) {
            is PeopleEvent.Internal.UserListLoaded -> {
                if (event.users.isNotEmpty()) {
                    Log.d("UsersLoaded not empty", "${event.users.size}")
                    state {
                        copy(
                            users = event.users,
                            isEmptyState = false,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    effects { PeopleEffect.UsersListEmpty }
                }
            }
            PeopleEvent.Internal.UserListLoadEmpty -> {
                if (state.isEmptyState) {
                    state { copy(isLoading = false, error = null) }
                    effects { PeopleEffect.UsersListEmpty }
                } else {
                    state { copy(isLoading = false, error = null) }
                }
            }
            is PeopleEvent.Internal.UserListLoadError -> {
                if (state.error != null) {
                    state { copy(isLoading = false, error = event.error) }
                    effects { PeopleEffect.UserListLoadError(event.error) }
                } else {
                    state { copy(isLoading = false, error = null) }
                }
            }
            is PeopleEvent.Ui.GetUserList -> {
                state {
                    copy(
                        isLoading = true,
                        error = null
                    )
                }
                commands { +PeopleCommand.LoadUserList(event.query) }
            }
        }
    }
}
