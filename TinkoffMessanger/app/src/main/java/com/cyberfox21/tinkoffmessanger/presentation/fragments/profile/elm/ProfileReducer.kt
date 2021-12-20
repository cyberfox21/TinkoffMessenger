package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

import com.cyberfox21.tinkoffmessanger.domain.enums.UserStatus
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.enums.ProfileMode
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ProfileReducer : DslReducer<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>() {
    override fun Result.reduce(event: ProfileEvent) {
        return when (event) {
            is ProfileEvent.Internal.UserLoaded -> {
                if (event.user == null) {
                    if (state.profileScreenMode == ProfileMode.YOUR && state.isEmptyState) {
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
                        state { copy(isLoading = false, error = null) }
                    }
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
                    commands { +state.user?.let { ProfileCommand.GetUserPresence(it.id) } }
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
                if (state.profileScreenMode == ProfileMode.YOUR)
                    state {
                        copy(
                            isLoading = true,
                            error = null,
                            profileScreenMode = ProfileMode.YOUR
                        )
                    }
                commands { +ProfileCommand.LoadCurrentUser }
            }
            is ProfileEvent.Ui.GetSelectedUser -> {
                state {
                    copy(
                        user = event.user,
                        isEmptyState = false,
                        isLoading = false,
                        error = null,
                        profileScreenMode = ProfileMode.STRANGER
                    )
                }
                commands { +state.user?.let { ProfileCommand.GetUserPresence(it.id) } }
            }
            is ProfileEvent.Internal.UserPresenceLoaded -> effects {
                if (state.userStatus != UserStatus.OFFLINE && event.status == UserStatus.OFFLINE) {
                } else {
                    state { copy(userStatus = event.status) }
                    event.status.let { +ProfileEffect.UserPresenceLoadedSuccess(it) }
                }
            }
            is ProfileEvent.Internal.UserPresenceLoadedError -> {
            }
        }
    }
}
