package com.cyberfox21.tinkoffmessanger.data.network.response

import com.cyberfox21.tinkoffmessanger.data.network.response.dto.SubscribedChannelDTO
import com.google.gson.annotations.SerializedName

data class SubscribedChannelsResponse(
    @SerializedName("subscriptions")
    val subscriptions: List<SubscribedChannelDTO>
)
