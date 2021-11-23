package com.cyberfox21.tinkoffmessanger.data.repository

import android.app.Application
import android.content.Context
import com.cyberfox21.tinkoffmessanger.data.api.ApiFactory
import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase
import com.cyberfox21.tinkoffmessanger.data.mapToTopic
import com.cyberfox21.tinkoffmessanger.data.mapToTopicDBModel
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.domain.repository.TopicsRepository
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class TopicsRepositoryImpl(application: Application) : TopicsRepository {

    private val api = ApiFactory.api

    private val topicsDao =
        AppDatabase.getInstance(application, AppDatabase.TOPICS_DB_NAME).topicsDao()

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

    override fun getTopics(channelId: Int): Flowable<List<Topic>> =
        getTopicsFromDB(channelId).toFlowable()
            .switchIfEmpty(getTopicsFromNetwork(channelId).toFlowable())

}