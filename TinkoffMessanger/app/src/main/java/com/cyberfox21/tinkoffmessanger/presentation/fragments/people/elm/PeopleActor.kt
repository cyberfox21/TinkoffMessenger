package com.cyberfox21.tinkoffmessanger.presentation.fragments.people.elm

import android.content.Context
import com.cyberfox21.tinkoffmessanger.data.repository.UsersRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetUsersListUseCase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import vivid.money.elmslie.core.ActorCompat

class PeopleActor(context: Context) : ActorCompat<PeopleCommand, PeopleEvent> {

    private var repository = UsersRepositoryImpl(context)

    private val getUsersListUseCase = GetUsersListUseCase(repository)

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