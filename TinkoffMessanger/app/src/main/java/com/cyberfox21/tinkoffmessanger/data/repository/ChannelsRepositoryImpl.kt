package com.cyberfox21.tinkoffmessanger.data.repository

import android.app.Application
import com.cyberfox21.tinkoffmessanger.data.api.ApiFactory
import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase
import com.cyberfox21.tinkoffmessanger.domain.entity.Channel
import com.cyberfox21.tinkoffmessanger.domain.repository.ChannelsRepository
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.Category
import com.cyberfox21.tinkoffmessanger.util.mapToChannel
import com.cyberfox21.tinkoffmessanger.util.mapToChannelDBModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ChannelsRepositoryImpl(application: Application) : ChannelsRepository {

    private val api = ApiFactory.api

    private val channelsDao =
        AppDatabase.getInstance(application, AppDatabase.CHANNELS_DB_NAME).channelsDao()

    private fun getChannelsList(): Single<List<Channel>> {
        var result: Single<List<Channel>>? = null
        val dbResult = channelsDao.getAllChannelsList().map { dbModels ->
            if (dbModels.isEmpty()) {
                result = api.getChannels().map { response ->
                    response.streams.map {
                        it.mapToChannel()
                    }
                }.doOnSuccess { channels ->
                    channelsDao.addChannelsListToDB(
                        channels.map {
                            it.mapToChannelDBModel(subscribed = false)
                        }
                    )
                }
            }
            dbModels.map {
                it.mapToChannel()
            }
        }.subscribeOn(Schedulers.io())
        if (result != null) {
            return result as Single<List<Channel>>
        }
        return dbResult

    }

    private fun getSubscribedChannelsList(): Single<List<Channel>> {
        var result: Single<List<Channel>>? = null
        val dbResult = channelsDao.getSubscribedChannelsList().map { dbModels ->
            if (dbModels.isEmpty()) {
                result = api.getSubscribedChannels().map { response ->
                    response.subscriptions.map {
                        it.mapToChannel()
                    }
                }.doOnSuccess { subscriptions ->
                    channelsDao.addChannelsListToDB(
                        subscriptions.map {
                            it.mapToChannelDBModel(subscribed = true)
                        }
                    )
                }
            }
            dbModels.map {
                it.mapToChannel()
            }
        }.subscribeOn(Schedulers.io())
        if (result != null) return result as Single<List<Channel>>
        return dbResult
    }

    override fun searchChannels(
        searchQuery: String,
        category: Category
    ): Single<List<Channel>> {
        return when (category) {
            Category.SUBSCRIBED -> getSubscribedChannelsList()
            Category.ALL -> getChannelsList()
        }
    }

}
