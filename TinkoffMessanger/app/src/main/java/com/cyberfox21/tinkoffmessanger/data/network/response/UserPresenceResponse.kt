package com.cyberfox21.tinkoffmessanger.data.network.response

import com.cyberfox21.tinkoffmessanger.data.network.response.dto.UserPresenceDTO
import com.google.gson.annotations.SerializedName

data class UserPresenceResponse(
    @SerializedName("result")
    val result: String,
    @SerializedName("msg")
    val message: String,
    @SerializedName("presence")
    val presence: UserPresenceDTO
)
