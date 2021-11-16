package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.data.api.ApiFactory
import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import com.cyberfox21.tinkoffmessanger.domain.repository.TopicsRepository
import com.cyberfox21.tinkoffmessanger.util.mapToTopic
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

object TopicsRepositoryImpl : TopicsRepository {

    private val api = ApiFactory.api

    override fun getTopics(channelId: Int): Single<List<Topic>> {
        return api.getChannelTopics(channelId)
            .map { response ->
                response.topics.map { it.mapToTopic() }
            }.doOnSuccess {
                // save to db
            }.subscribeOn(Schedulers.io())
    }

}