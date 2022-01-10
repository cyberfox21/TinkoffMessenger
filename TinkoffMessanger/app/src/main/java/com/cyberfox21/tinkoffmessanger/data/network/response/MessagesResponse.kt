package com.cyberfox21.tinkoffmessanger.data.network.response

import com.cyberfox21.tinkoffmessanger.data.network.response.dto.MessageDTO
import com.google.gson.annotations.SerializedName

data class MessagesResponse(
    @SerializedName("found_anchor")
    val foundAnchor: Boolean,
    @SerializedName("found_newest")
    val foundNewest: Boolean,
    @SerializedName("messages")
    val messages: List<MessageDTO>
)
