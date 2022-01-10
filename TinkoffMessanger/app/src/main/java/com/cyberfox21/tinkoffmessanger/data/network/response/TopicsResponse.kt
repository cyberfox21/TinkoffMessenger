package com.cyberfox21.tinkoffmessanger.data.network.response

import com.cyberfox21.tinkoffmessanger.data.network.response.dto.TopicDTO
import com.google.gson.annotations.SerializedName

data class TopicsResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("msg")
    val message: String,
    @SerializedName("topics")
    val topics: List<TopicDTO>
)
