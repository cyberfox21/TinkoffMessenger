package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cyberfox21.tinkoffmessanger.data.repository.ChannelsRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.enums.Category
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetChannelsListUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.SearchChannelsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class ChannelsViewModel : ViewModel() {

    private val repository = ChannelsRepositoryImpl

    private val getChannelsListUseCase = GetChannelsListUseCase(repository)
    private val searchChannelsUseCase = SearchChannelsUseCase(repository)

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchSubject: BehaviorSubject<String> =
        BehaviorSubject.createDefault(INITIAL_QUERY)

    private var _channelsScreenState: MutableLiveData<ChannelsScreenState> = MutableLiveData()
    val channelsScreenState: LiveData<ChannelsScreenState>
        get() = _channelsScreenState

    var channelsList = listOf<Channel>()

    private var category: Category = Category.SUBSCRIBED

    init {
        subscribeToSearchChanges()
    }

    fun searchChannels(category: Category, searchQuery: String) {
        this.category = category
        searchSubject.onNext(searchQuery)
    }

    private fun subscribeToSearchChanges() {
        searchSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .doOnNext { _channelsScreenState.postValue(ChannelsScreenState.Loading) }
            .debounce(DEBOUNCE_DURATION, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMap { searchQuery -> searchChannelsUseCase(searchQuery, category) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    channelsList = it
                    _channelsScreenState.value = ChannelsScreenState.Result(it)
                },
                onError = { _channelsScreenState.value = ChannelsScreenState.Error(it) }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val INITIAL_QUERY: String = ""
        private const val DEBOUNCE_DURATION: Long = 500L
    }

}
