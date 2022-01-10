package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.database.dao.TopicsDao
import com.cyberfox21.tinkoffmessanger.data.mapToTopic
import com.cyberfox21.tinkoffmessanger.data.mapToTopicDBModel
import com.cyberfox21.tinkoffmessanger.data.network.api.TopicsApi
import com.cyberfox21.tinkoffmessanger.di.qualifier.TopicsApiQualifier
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.domain.repository.TopicsRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TopicsRepositoryImpl @Inject constructor(
    @TopicsApiQualifier private val api: TopicsApi,
    private val topicsDao: TopicsDao
) : TopicsRepository {

    private fun getTopicsFromDB(channelId: Int): Single<List<Topic>> {
        return topicsDao.getTopicsListForChannel(channelId)
            .map { dbModels -> dbModels.map { it.mapToTopic() } }
            .subscribeOn(Schedulers.io())
    }

    private fun getTopicsFromNetwork(channelId: Int): Single<List<Topic>> =
        api.getChannelTopics(channelId)
            .map { response -> response.topics.map { it.mapToTopic() } }
            .doOnSuccess { topics ->
                topicsDao.addTopicsListToDB(
                    topics.map {
                        it.mapToTopicDBModel(channelId)
                    }
                )
            }
            .onErrorReturn { listOf() }
            .subscribeOn(Schedulers.io())

    override fun getTopics(channelId: Int): Observable<List<Topic>> {
        return Observable.concat(
            getTopicsFromNetwork(channelId).toObservable(),
            getTopicsFromDB(channelId).toObservable()
        ).subscribeOn(Schedulers.io())
    }
}
