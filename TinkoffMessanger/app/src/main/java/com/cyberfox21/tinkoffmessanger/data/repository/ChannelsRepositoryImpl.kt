package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.Api
import com.cyberfox21.tinkoffmessanger.data.database.dao.AllChannelsDao
import com.cyberfox21.tinkoffmessanger.data.database.dao.SubscribedChannelsDao
import com.cyberfox21.tinkoffmessanger.data.mapToChannel
import com.cyberfox21.tinkoffmessanger.data.mapToChannelDBModel
import com.cyberfox21.tinkoffmessanger.data.mapToSubscribedChannelDBModel
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.repository.ChannelsRepository
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.Category
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChannelsRepositoryImpl @Inject constructor(
    private val api: Api,
    private val channelsDao: AllChannelsDao,
    private val subscribedChannelsDao: SubscribedChannelsDao
) : ChannelsRepository {

    private fun getAllChannelsFromDB(): Single<List<Channel>> =
        channelsDao.getAllChannelsList().map { dbModels ->
            dbModels.map {
                it.mapToChannel()
            }
        }.subscribeOn(Schedulers.io())

    // todo check internet is available
    private fun getAllChannelsFromNetwork(): Single<List<Channel>> =
        api.getChannels().map { response ->
            response.streams.map {
                it.mapToChannel()
            }
        }.doOnSuccess { channels ->
            channelsDao.addAllChannelsListToDB(
                channels.map {
                    it.mapToChannelDBModel()
                }
            )
        }.subscribeOn(Schedulers.io())

    private fun getSubscribedFromDB(): Single<List<Channel>> {
        return subscribedChannelsDao.getSubscribedChannelsList().map { dbModels ->
            dbModels.map {
                it.mapToChannel()
            }
        }.subscribeOn(Schedulers.io())
    }

    private fun getSubscribedFromNetwork(): Single<List<Channel>> {
        // todo check internet is available
        return api.getSubscribedChannels().map { response ->
            response.subscriptions.map {
                it.mapToChannel()
            }
        }.doOnSuccess { subscriptions ->
            subscribedChannelsDao.addSubscribedChannelsListToDB(
                subscriptions.map {
                    it.mapToSubscribedChannelDBModel()
                }
            )
        }.subscribeOn(Schedulers.io())
    }

    private fun getChannelsList(): Observable<List<Channel>> {
//        return Observable.concat(
//            getAllChannelsFromDB().toObservable(),
//            getAllChannelsFromNetwork().toObservable()
//        ).distinct()
        return getAllChannelsFromNetwork().toObservable()
    }

    private fun getSubscribedChannelsList(): Observable<List<Channel>> {
//        return Observable.concat(
//            getSubscribedFromDB().toObservable(),
//            getSubscribedFromNetwork().toObservable()
//        ).distinct()
        return getSubscribedFromNetwork().toObservable()
    }

    override fun searchChannels(
        searchQuery: String,
        category: Category
    ): Observable<List<Channel>> {
        return when (category) {
            Category.SUBSCRIBED -> getSubscribedChannelsList()
            Category.ALL -> getChannelsList()
        }
    }
}
