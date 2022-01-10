package com.cyberfox21.tinkoffmessanger.data.network.api

import com.cyberfox21.tinkoffmessanger.data.network.response.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface ReactionsApi {

    @GET("/static/generated/emoji/emoji_codes.json")
    fun getReactions(): Single<ReactionsResponse>

    @FormUrlEncoded
    @POST("messages/{message_id}/reactions")
    fun addReaction(
        @Path("message_id") messageId: Int,
        @Field("emoji_name") emojiName: String,
    ): Completable

    @DELETE("messages/{message_id}/reactions")
    fun deleteReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String,
        @Query("emoji_code") emojiCode: String,
        @Query("reaction_type") emojiType: String
    ): Completable

}
