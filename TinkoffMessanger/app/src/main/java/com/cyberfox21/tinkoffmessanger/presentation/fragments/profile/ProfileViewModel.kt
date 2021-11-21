package com.cyberfox21.tinkoffmessanger.presentation.fragments.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cyberfox21.tinkoffmessanger.data.repository.UsersRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetMyUserUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsersRepositoryImpl(application)

    private val getMyUserUseCase = GetMyUserUseCase(repository)

    private val compositeDisposable = CompositeDisposable()

    private val _userLD = MutableLiveData<ProfileScreenState>()
    val userLD get() = _userLD

    init {
        subscribeToGetCurrentUser()
    }

    private fun subscribeToGetCurrentUser() {
        getMyUserUseCase()
            .subscribeOn(Schedulers.io())
            .doOnNext { _userLD.postValue(ProfileScreenState.Loading) }
            .subscribeBy(
                onNext = { _userLD.postValue(ProfileScreenState.Result(it)) },
                onError = { _userLD.postValue(ProfileScreenState.Error(it)) }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}