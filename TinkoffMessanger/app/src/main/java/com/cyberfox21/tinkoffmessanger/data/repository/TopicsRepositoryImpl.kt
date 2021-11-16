package com.cyberfox21.tinkoffmessanger.data.repository

import android.app.Application
import com.cyberfox21.tinkoffmessanger.data.api.ApiFactory
import com.cyberfox21.tinkoffmessanger.data.database.AppDatabase
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.domain.repository.TopicsRepository
import com.cyberfox21.tinkoffmessanger.util.mapToTopic
import com.cyberfox21.tinkoffmessanger.util.mapToTopicDBModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TopicsRepositoryImpl(application: Application) : TopicsRepository {

    private val api = ApiFactory.api

    private val topicsDao =
        AppDatabase.getInstance(application, AppDatabase.TOPICS_DB_NAME).topicsDao()

    override fun getTopics(channelId: Int): Single<List<Topic>> {
        var result : Single<List<Topic>>? = null
        val dbResult = topicsDao.getTopicsListForChannel(channelId).map { dbModels ->
            if(dbModels.isEmpty()){
                result = api.getChannelTopics(channelId)
                    .map { response ->
                        response.topics.map { it.mapToTopic() }
                    }.doOnSuccess { topics ->
                        topicsDao.addTopicsListToDB(
                            topics.map {
                                it.mapToTopicDBModel(channelId)
                            }
                        )
                    }.subscribeOn(Schedulers.io())
            }
            dbModels.map {
                it.mapToTopic()
            }
        }.subscribeOn(Schedulers.io())
        if(result != null){
            return result as Single<List<Topic>>
        }
        return dbResult
    }

}