package com.cyberfox21.tinkoffmessanger.data.network.response.dto

import com.google.gson.annotations.SerializedName

class ChannelDTO(
    @SerializedName("name")
    val name: String,
    @SerializedName("stream_id")
    val streamId: Int
)
