package com.cyberfox21.tinkoffmessanger.data.api.response

import com.cyberfox21.tinkoffmessanger.data.api.response.dto.TopicDTO
import com.google.gson.annotations.SerializedName

data class TopicsResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("msg")
    val message: String,
    @SerializedName("topics")
    val topics: List<TopicDTO>
)
