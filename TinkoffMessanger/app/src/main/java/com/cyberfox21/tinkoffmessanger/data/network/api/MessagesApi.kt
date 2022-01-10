package com.cyberfox21.tinkoffmessanger.data.network.api

import com.cyberfox21.tinkoffmessanger.data.network.response.MessagesResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface MessagesApi {

    @GET("messages?anchor=newest")
    fun getMessages(
        @Query("num_before") messagesNumberBefore: Int,
        @Query("num_after") messagesNumberAfter: Int,
        @Query("narrow") narrowFilterArray: String,
    ): Single<MessagesResponse>

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: Int,
        @Query("num_before") messagesNumberBefore: Int,
        @Query("num_after") messagesNumberAfter: Int,
        @Query("narrow") narrowFilterArray: String,
    ): Single<MessagesResponse>

    @FormUrlEncoded
    @POST("messages")
    fun sendMessage(
        @Field("to") channel: String,
        @Field("topic") topic: String,
        @Field("content") content: String,
        @Field("type") type: String = "stream",
    ): Completable

    @FormUrlEncoded
    @PATCH("messages/{message_id}")
    fun editMessage(
        @Path("message_id") message_id: Int,
        @Field("content") content: String
    ): Completable

    @FormUrlEncoded
    @PATCH("messages/{message_id}")
    fun changeMessageTopic(
        @Path("message_id") message_id: Int,
        @Field("topic") topic: String
    ): Completable

    @DELETE("messages/{msg_id}")
    fun deleteMessage(@Path("msg_id") msgId: Int): Completable
}