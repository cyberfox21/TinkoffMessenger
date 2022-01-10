package com.cyberfox21.tinkoffmessanger.data.network.response.dto

import com.google.gson.annotations.SerializedName

data class MessageDTO(

    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("sender_id")
    val senderId: Int,
    @SerializedName("sender_email")
    val senderEmail: String,
    @SerializedName("sender_full_name")
    val senderFullName: String,
    @SerializedName("sender_realm_str")
    val senderRealmStr: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("content_type")
    val contentType: String,
    @SerializedName("reactions")
    val reactions: List<ReactionDTO>,
    @SerializedName("is_me_message")
    val isMeMessage: Boolean,
    @SerializedName("recipient_id")
    val recipientId: Int,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("timestamp")
    val timestamp: Int,
    @SerializedName("type")
    val type: String
)
