package com.cyberfox21.tinkoffmessanger.data.network.response

import com.cyberfox21.tinkoffmessanger.data.network.response.dto.ChannelDTO
import com.google.gson.annotations.SerializedName

data class ChannelsResponse(
    @SerializedName("streams")
    val streams: List<ChannelDTO>
)
