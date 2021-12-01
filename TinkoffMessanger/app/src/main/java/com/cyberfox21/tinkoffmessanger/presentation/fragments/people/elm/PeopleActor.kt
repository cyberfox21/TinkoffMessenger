package com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm

import com.cyberfox21.tinkoffmessanger.domain.usecase.GetUsersListUseCase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vivid.money.elmslie.core.ActorCompat

class PeopleActor(private val getUsersListUseCase: GetUsersListUseCase) :
    ActorCompat<PeopleCommand, PeopleEvent> {

    override fun execute(command: PeopleCommand): Observable<PeopleEvent> {
        return when (command) {
            is PeopleCommand.LoadUserList -> {
                getUsersListUseCase().mapEvents(
                    { users -> PeopleEvent.Internal.UserListLoaded(users) },
                    { error -> PeopleEvent.Internal.UserListLoadError(error) }
                ).subscribeOn(Schedulers.io())
            }
        }
    }

}