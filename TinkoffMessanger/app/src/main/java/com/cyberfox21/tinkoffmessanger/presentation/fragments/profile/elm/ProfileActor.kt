package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

import com.cyberfox21.tinkoffmessanger.domain.enums.UserStatus
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetMyUserUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetUserPresenceUseCase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vivid.money.elmslie.core.ActorCompat

class ProfileActor(
    private val getMyUserUseCase: GetMyUserUseCase,
    private val getUserPresenceUseCase: GetUserPresenceUseCase
) :
    ActorCompat<ProfileCommand, ProfileEvent> {

    override fun execute(command: ProfileCommand): Observable<ProfileEvent> {
        return when (command) {
            is ProfileCommand.LoadCurrentUser -> getMyUserUseCase()
                .subscribeOn(Schedulers.io())
                .mapEvents(
                    { user -> ProfileEvent.Internal.UserLoaded(user.getOrNull()) },
                    { error -> ProfileEvent.Internal.ErrorLoading(error) }
                )
            is ProfileCommand.GetUserPresence -> getUserPresenceUseCase(command.userId)
                .subscribeOn(Schedulers.io())
                .map { result ->
                    result.fold(
                        { ProfileEvent.Internal.UserPresenceLoaded(it) },
                        { ProfileEvent.Internal.UserPresenceLoaded(UserStatus.OFFLINE) }
                    )
                }
        }
    }
}
