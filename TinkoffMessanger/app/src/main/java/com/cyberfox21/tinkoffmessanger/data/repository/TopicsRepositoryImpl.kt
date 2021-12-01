package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.Api
import com.cyberfox21.tinkoffmessanger.data.database.dao.TopicsDao
import com.cyberfox21.tinkoffmessanger.data.mapToTopic
import com.cyberfox21.tinkoffmessanger.data.mapToTopicDBModel
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.domain.repository.TopicsRepository
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TopicsRepositoryImpl @Inject constructor(
    private val api: Api,
    private val topicsDao: TopicsDao
) : TopicsRepository {

    private fun getTopicsFromDB(channelId: Int) =
        topicsDao.getTopicsListForChannel(channelId).map { dbModels ->
            dbModels.map {
                it.mapToTopic()
            }
        }.subscribeOn(Schedulers.io())

    // todo check internet is available
    private fun getTopicsFromNetwork(channelId: Int) =
        api.getChannelTopics(channelId)
            .map { response ->
                response.topics.map { it.mapToTopic() }
            }.doOnSuccess { topics ->
                topicsDao.addTopicsListToDB(
                    topics.map {
                        it.mapToTopicDBModel(channelId)
                    }
                )
            }.subscribeOn(Schedulers.io())

    override fun getTopics(channelId: Int): Flowable<List<Topic>> {
//        return getTopicsFromDB(channelId).toFlowable()
//            .switchIfEmpty(getTopicsFromNetwork(channelId).toFlowable())
        return getTopicsFromNetwork(channelId).toFlowable()
    }
}