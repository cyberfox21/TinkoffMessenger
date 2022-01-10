package com.cyberfox21.tinkoffmessanger.data.network.response.dto

import com.google.gson.annotations.SerializedName

data class UserPresenceClientDTO(
    @SerializedName("status")
    val status: String
)