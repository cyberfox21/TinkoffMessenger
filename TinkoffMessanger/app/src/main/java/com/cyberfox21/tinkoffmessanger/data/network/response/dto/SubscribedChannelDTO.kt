package com.cyberfox21.tinkoffmessanger.data.network.response.dto

import com.google.gson.annotations.SerializedName

data class SubscribedChannelDTO(
    @SerializedName("audible_notifications")
    val audible_notifications: Boolean,
    @SerializedName("color")
    val color: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("invite_only")
    val invite_only: Boolean,
    @SerializedName("is_muted")
    val is_muted: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("stream_id")
    val streamId: Int,
    @SerializedName("subscribers")
    val subscribers: List<Int>
)
