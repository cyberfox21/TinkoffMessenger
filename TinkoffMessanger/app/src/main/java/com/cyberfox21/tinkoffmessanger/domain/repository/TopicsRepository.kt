package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import io.reactivex.Single

interface TopicsRepository {

    fun getTopics(channelId: Int): Single<List<Topic>>

}