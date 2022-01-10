package com.cyberfox21.tinkoffmessanger.data.network.api

import com.cyberfox21.tinkoffmessanger.data.network.response.ChannelsResponse
import com.cyberfox21.tinkoffmessanger.data.network.response.SubscribedChannelsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ChannelsApi {

    @GET("streams")
    fun getChannels(): Single<ChannelsResponse>

    @GET("users/me/subscriptions")
    fun getSubscribedChannels(): Single<SubscribedChannelsResponse>

}