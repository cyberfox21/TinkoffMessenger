package com.cyberfox21.tinkoffmessanger.data.api.response.dto

import com.google.gson.annotations.SerializedName

class ChannelDTO(
    @SerializedName("description")
    val description: String,
    @SerializedName("invite_only")
    val invite_only: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("stream_id")
    val streamId: Int
)
