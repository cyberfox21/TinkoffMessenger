package com.cyberfox21.tinkoffmessanger.domain.repository

import com.cyberfox21.tinkoffmessanger.domain.entity.Topic
import io.reactivex.Observable

interface TopicsRepository {

    fun getTopics(channelId: Int): Observable<List<Topic>>

}