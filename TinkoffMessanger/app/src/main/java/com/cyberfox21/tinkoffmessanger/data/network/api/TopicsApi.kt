package com.cyberfox21.tinkoffmessanger.data.network.api

import com.cyberfox21.tinkoffmessanger.data.network.response.TopicsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TopicsApi {
    @GET("users/me/{stream_id}/topics")
    fun getChannelTopics(@Path("stream_id") channelId: Int): Single<TopicsResponse>
}