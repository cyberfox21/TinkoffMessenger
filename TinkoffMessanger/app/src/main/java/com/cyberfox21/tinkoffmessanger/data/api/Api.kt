package com.cyberfox21.tinkoffmessanger.data.api

import com.cyberfox21.tinkoffmessanger.data.api.dto.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface Api {

    @GET("messages")
    fun getMessages(
        @Query("narrow") narrowFilterArray : String,
        @Query("anchor") anchor : String,
        @Query("num_before") messagesNumberBefore : Int = 100,
        @Query("num_after") messagesNumberAfter : Int = 0,
    ): Single<MessagesResponse>

    @GET("users")
    fun getUsers(): Single<UsersResponse>

    @GET("users/{user_id}")
    fun getUser(
        @Path("user_id") id: Int
    ):Single<UsersResponse>

    @GET("users/me")
    fun getMyUser(): Single<UserDTO>

    @GET("users/{user_id_or_email}/presence")
    fun getUserPresence(
        @Path("user_id_or_email") userIdOrEmail: String
    ) : Single<UserPresenceResponse>

//    @GET("oneStreams")
//    fun getAllStreams(): Single<StreamsResponse>
//
//    @GET("users/me/{stream_id}/oneTopics")
//    fun getTopicsByStreamId(@Path("stream_id") id: Int): Single<TopicsResponse>

    @FormUrlEncoded
    @POST("messages")
    fun sendMessageToStream(
        @Field("to") streamIdOrName: String,
        @Field("content") content: String,
        @Field("topic") topic: String,
        @Field("type") type: String = "stream",
    ) : Single<SendMessageResponse>

    @FormUrlEncoded
    @POST("messages")
    fun sendPrivateMessage(
        @Field("to") recipientsIdIntArray: String,
        @Field("content") content: String,
        @Field("type") type: String = "private",
    ) : Single<SendMessageResponse>

    @FormUrlEncoded
    @POST("messages/{message_id}/reactions")
    fun addReaction(
        @Path("message_id") messageId: Int,
        @Field("emoji_name") emojiName: String,
    ) : Completable

}