package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.Api
import com.cyberfox21.tinkoffmessanger.data.database.dao.AllChannelsDao
import com.cyberfox21.tinkoffmessanger.data.database.dao.SubscribedChannelsDao
import com.cyberfox21.tinkoffmessanger.data.mapToChannel
import com.cyberfox21.tinkoffmessanger.data.mapToChannelDBModel
import com.cyberfox21.tinkoffmessanger.data.mapToSubscribedChannelDBModel
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.repository.ChannelsRepository
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.enums.Category
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChannelsRepositoryImpl @Inject constructor(
    private val api: Api,
    private val channelsDao: AllChannelsDao,
    private val subscribedChannelsDao: SubscribedChannelsDao
) : ChannelsRepository {

    override fun searchChannels(
        searchQuery: String,
        category: Category
    ): Observable<Result<List<Channel>>> = when (category) {
        Category.SUBSCRIBED -> getSubscribedChannelsList()
        Category.ALL -> getChannelsList()
    }

    private fun getChannelsList(): Observable<Result<List<Channel>>> {
        return Observable.concat(
            getAllChannelsFromNetwork().toObservable(),
            getAllChannelsFromDB().toObservable()
        ).subscribeOn(Schedulers.io())
    }

    private fun getSubscribedChannelsList(): Observable<Result<List<Channel>>> {
        return Observable.concat(
            getSubscribedFromNetwork().toObservable(),
            getSubscribedFromDB().toObservable()
        ).subscribeOn(Schedulers.io())
    }

    private fun getAllChannelsFromDB(): Single<Result<List<Channel>>> {
        return channelsDao.getAllChannelsList()
            .map { dbModels ->
                Result.success(dbModels.map { it.mapToChannel() })
            }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())
    }

    private fun getAllChannelsFromNetwork(): Single<Result<List<Channel>>> {
        return api.getChannels()
            .map { response ->
                Result.success(response.streams.map { it.mapToChannel() })
            }
            .onErrorReturn { Result.failure(it) }
            .doOnSuccess { result ->
                result.getOrNull()?.map { it.mapToChannelDBModel() }
                    ?.let { channelsDao.addAllChannelsListToDB(it) }
            }
            .subscribeOn(Schedulers.io())
    }

    private fun getSubscribedFromDB(): Single<Result<List<Channel>>> {
        return subscribedChannelsDao.getSubscribedChannelsList()
            .map { dbModels ->
                Result.success(dbModels.map { it.mapToChannel() })
            }
            .onErrorReturn { Result.failure(it) }
            .subscribeOn(Schedulers.io())
    }

    private fun getSubscribedFromNetwork(): Single<Result<List<Channel>>> {
        return api.getSubscribedChannels()
            .map { response ->
                Result.success(response.subscriptions.map { it.mapToChannel() })
            }
            .onErrorReturn { Result.failure(it) }
            .doOnSuccess { subscriptions ->
                subscriptions.getOrNull()?.map {
                    it.mapToSubscribedChannelDBModel()
                }?.let { subscribedChannelsDao.addSubscribedChannelsListToDB(it) }
            }
            .subscribeOn(Schedulers.io())
    }
}
