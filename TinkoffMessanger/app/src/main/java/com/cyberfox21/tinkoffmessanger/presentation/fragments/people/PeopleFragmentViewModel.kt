package com.cyberfox21.tinkoffmessanger.presentation.fragments.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cyberfox21.tinkoffmessanger.data.repository.UsersRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.entity.User
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetUsersListUseCase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PeopleFragmentViewModel : ViewModel() {

    private var repository = UsersRepositoryImpl

    private val getUsersListUseCase = GetUsersListUseCase(repository)

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val observer: Single<List<User>> = getUsersListUseCase()

    private var _peopleScreenState: MutableLiveData<UsersScreenState> = MutableLiveData()
    val peopleScreenState: LiveData<UsersScreenState>
        get() = _peopleScreenState

    init {
        subscribeToUsersChanges()
    }

    private fun subscribeToUsersChanges() {
        observer
            .subscribeOn(Schedulers.io())
            .doOnSuccess { _peopleScreenState.postValue(UsersScreenState.Loading) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _peopleScreenState.value = UsersScreenState.Result(it)
                },
                onError = {
                    _peopleScreenState.value = UsersScreenState.Error(it)
                }
            ).addTo(compositeDisposable)

    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}
