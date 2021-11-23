package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.elm

import android.content.Context
import com.cyberfox21.tinkoffmessanger.data.repository.UsersRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetMyUserUseCase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vivid.money.elmslie.core.ActorCompat

class ProfileActor(context: Context) : ActorCompat<ProfileCommand, ProfileEvent> {

    private val repository = UsersRepositoryImpl(context)

    private val getMyUserUseCase = GetMyUserUseCase(repository)

    override fun execute(command: ProfileCommand): Observable<ProfileEvent> {
        return when (command) {
            is ProfileCommand.LoadCurrentUser -> getMyUserUseCase()
                .subscribeOn(Schedulers.io())
                .toObservable()
                .mapEvents(
                    { user -> ProfileEvent.Internal.UserLoaded(user) },
                    { error -> ProfileEvent.Internal.ErrorLoading(error) }
                )
        }
    }

}