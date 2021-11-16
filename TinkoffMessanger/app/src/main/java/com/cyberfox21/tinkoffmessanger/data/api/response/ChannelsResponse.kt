package com.cyberfox21.tinkoffmessanger.data.api.response

import com.cyberfox21.tinkoffmessanger.data.api.response.dto.ChannelDTO
import com.google.gson.annotations.SerializedName

data class ChannelsResponse(
    @SerializedName("streams")
    val streams: List<ChannelDTO>
)
