package com.cyberfox21.tinkoffmessanger.presentation.fragments.people

import androidx.lifecycle.ViewModel
import com.cyberfox21.tinkoffmessanger.data.repository.UsersRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetUsersListUseCase

class PeopleFragmentViewModel : ViewModel() {

    private var repository = UsersRepositoryImpl

    private val getUsersListUseCase = GetUsersListUseCase(repository)

    var users = getUsersListUseCase()

}
