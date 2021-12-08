package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.ProfileMode
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ProfileReducer : DslReducer<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>() {
    override fun Result.reduce(event: ProfileEvent) {
        return when (event) {
            is ProfileEvent.Internal.UserLoaded -> {
                if (event.user == null) {
                    state {
                        copy(
                            user = null,
                            isEmptyState = true,
                            isLoading = false,
                            error = null,
                            profileScreenMode = ProfileMode.YOUR
                        )
                    }
                    effects { ProfileEffect.UserEmpty }
                } else {
                    state {
                        copy(
                            user = event.user,
                            isEmptyState = false,
                            isLoading = false,
                            error = null,
                            profileScreenMode = ProfileMode.YOUR
                        )
                    }
                }
            }
            is ProfileEvent.Internal.ErrorLoading -> {
                state {
                    copy(
                        user = null,
                        isEmptyState = false,
                        isLoading = false,
                        error = event.error,
                        profileScreenMode = ProfileMode.YOUR
                    )
                }
                effects { ProfileEffect.UserLoadError(event.error) }
            }
            is ProfileEvent.Ui.GetCurrentUser -> {
                state {
                    copy(
                        user = null,
                        isEmptyState = false,
                        isLoading = true,
                        error = null,
                        profileScreenMode = ProfileMode.YOUR
                    )
                }
                commands { +ProfileCommand.LoadCurrentUser }
            }
            is ProfileEvent.Ui.GetSelectedUser -> { // сразу показываем результат
                state {
                    copy(
                        user = event.user,
                        isEmptyState = false,
                        isLoading = false,
                        error = null,
                        profileScreenMode = ProfileMode.STRANGER
                    )
                }
            }
        }
    }
}
