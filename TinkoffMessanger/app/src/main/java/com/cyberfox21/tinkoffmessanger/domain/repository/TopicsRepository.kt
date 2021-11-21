package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import io.reactivex.Flowable

interface TopicsRepository {

    fun getTopics(channelId: Int): Flowable<List<Topic>>

}