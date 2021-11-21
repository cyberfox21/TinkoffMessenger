package com.cyberfox21.tinkoffmessanger.presentation.fragments.people

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cyberfox21.tinkoffmessanger.data.repository.UsersRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetUsersListUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PeopleFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private var repository = UsersRepositoryImpl(application)

    private val getUsersListUseCase = GetUsersListUseCase(repository)

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var _peopleScreenState: MutableLiveData<UsersScreenState> = MutableLiveData()
    val peopleScreenState: LiveData<UsersScreenState>
        get() = _peopleScreenState

    init {
        subscribeToUsersChanges()
    }

    private fun subscribeToUsersChanges() {
        getUsersListUseCase()
            ?.subscribeOn(Schedulers.io())
            ?.doOnNext { _peopleScreenState.postValue(UsersScreenState.Loading) }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeBy(
                onNext = {
                    _peopleScreenState.value = UsersScreenState.Result(it)
                },
                onError = {
                    _peopleScreenState.value = UsersScreenState.Error(it)
                }
            )?.addTo(compositeDisposable)

    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}
