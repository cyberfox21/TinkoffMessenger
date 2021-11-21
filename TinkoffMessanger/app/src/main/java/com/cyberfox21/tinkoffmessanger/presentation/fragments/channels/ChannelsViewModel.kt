package com.cyberfox21.tinkoffmessanger.presentation.fragments.channels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cyberfox21.tinkoffmessanger.data.repository.ChannelsRepositoryImpl
import com.cyberfox21.tinkoffmessanger.data.repository.TopicsRepositoryImpl
import com.cyberfox21.tinkoffmessanger.domain.usecase.GetTopicsUseCase
import com.cyberfox21.tinkoffmessanger.domain.usecase.SearchChannelsUseCase
import com.cyberfox21.tinkoffmessanger.presentation.commondelegate.DelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.delegate.item.ChannelDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.mapToChannelDelegateItem
import com.cyberfox21.tinkoffmessanger.presentation.mapToTopicDelegateItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.BehaviorSubject.createDefault
import java.util.concurrent.TimeUnit

class ChannelsViewModel(application: Application) : AndroidViewModel(application) {

    private val channelsRepository = ChannelsRepositoryImpl(application)
    private val topicsRepository = TopicsRepositoryImpl(application)

    private val searchChannelsUseCase = SearchChannelsUseCase(channelsRepository)

    private val getTopicsUseCase = GetTopicsUseCase(topicsRepository)

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchSubject: BehaviorSubject<String> = createDefault(INITIAL_QUERY)

    private var _channelsScreenState: MutableLiveData<ChannelsScreenState> = MutableLiveData()
    val channelsScreenState: LiveData<ChannelsScreenState>
        get() = _channelsScreenState

    private var channelsList = mutableListOf<ChannelDelegateItem>()

    private var category: Category = Category.SUBSCRIBED

    var selectedChannelName: String? = null

    init {
        start()
    }

    fun start() {
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
                onNext = { listOfChannels ->
                    val delegateItemsList = listOfChannels.map {
                        it.mapToChannelDelegateItem(selected = false)
                    }.toMutableList()

                    channelsList = delegateItemsList
                    _channelsScreenState.postValue(
                        ChannelsScreenState.Result(delegateItemsList.toMutableList())
                    )
                },
                onError = { _channelsScreenState.value = ChannelsScreenState.Error(it) }
            )
            .addTo(compositeDisposable)
    }

    fun updateTopics(channelId: Int, isSelected: Boolean) {
        val delegateItemsList: MutableList<DelegateItem> = channelsList.toMutableList()

        if (isSelected) {
            _channelsScreenState.postValue(ChannelsScreenState.Result(delegateItemsList))
        } else {
            getTopicsUseCase(channelId).subscribeBy(
                onNext = { topics ->
                    val elementPosition =
                        delegateItemsList.indexOfFirst { it is ChannelDelegateItem && it.id == channelId }
                    val channel = delegateItemsList[elementPosition]
                    delegateItemsList.removeAt(elementPosition)
                    delegateItemsList.add(
                        elementPosition,
                        (channel as ChannelDelegateItem).copy(isSelected = true)
                    )
                    delegateItemsList.addAll(elementPosition + 1, topics.map {
                        it.mapToTopicDelegateItem()
                    })
                    _channelsScreenState.postValue(ChannelsScreenState.Result(delegateItemsList))
                },
                onError = { _channelsScreenState.postValue(ChannelsScreenState.Error(it)) }
            ).addTo(compositeDisposable)
        }
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
