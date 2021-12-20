package com.cyberfox21.tinkoffmessanger.data.api.response.dto

import com.google.gson.annotations.SerializedName

data class UserPresenceDTO(
    @SerializedName("aggregated")
    val client: UserPresenceClientDTO
)