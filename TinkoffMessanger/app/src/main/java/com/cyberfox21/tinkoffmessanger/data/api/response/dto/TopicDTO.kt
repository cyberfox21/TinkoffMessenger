package com.cyberfox21.tinkoffmessanger.data.api.response.dto

import com.google.gson.annotations.SerializedName

data class TopicDTO(
    @SerializedName("name")
    val name: String,
    @SerializedName("max_id")
    val lastMsgId: Int
)
