package com.cyberfox21.tinkoffmessanger.data.api

import com.cyberfox21.tinkoffmessanger.data.api.response.*
import com.cyberfox21.tinkoffmessanger.data.api.response.dto.UserDTO
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.*

interface Api {

    @GET("messages?anchor=newest")
    fun getMessages(
        @Query("num_before") messagesNumberBefore: Int,
        @Query("num_after") messagesNumberAfter: Int,
        @Query("narrow") narrowFilterArray: String,
    ): Single<MessagesResponse>

    @GET("users")
    fun getUsers(): Single<UsersResponse>

    @GET("users/{user_id}")
    fun getUser(
        @Path("user_id") id: Int
    ): Maybe<UserDTO>

    @GET("users/me")
    fun getMyUser(): Maybe<UserDTO>

    @GET("users/{user_id}/presence")
    fun getUserPresence(
        @Path(value = "user_id")
        userId: Int
    ): Single<UserPresenceResponse>

    @GET("streams")
    fun getChannels(): Single<ChannelsResponse>

    @GET("users/me/subscriptions")
    fun getSubscribedChannels(): Single<SubscribedChannelsResponse>

    @GET("users/me/{stream_id}/topics")
    fun getChannelTopics(
        @Path(value = "stream_id")
        channelId: Int
    ): Single<TopicsResponse>

    @GET("/static/generated/emoji/emoji_codes.json")
    fun getReactions(): Single<ReactionsResponse>

    @FormUrlEncoded
    @POST("messages")
    fun sendMessageToChannel(
        @Field("to") channel: String,
        @Field("topic") topic: String,
        @Field("content") content: String,
        @Field("type") type: String = "stream",
    ): Completable

    @FormUrlEncoded
    @POST("messages")
    fun sendPrivateMessage(
        @Field("to") recipientsIdIntArray: String,
        @Field("content") content: String,
        @Field("type") type: String = "private",
    ): Completable

    @FormUrlEncoded
    @POST("messages/{message_id}/reactions")
    fun addReaction(
        @Path("message_id") messageId: Int,
        @Field("emoji_name") emojiName: String,
    ): Completable


    @DELETE("messages/{message_id}/reactions")
    fun deleteReaction(
        @Path("message_id")
        messageId: Int,
        @Query("emoji_name")
        emojiName: String
    ): Completable

}
