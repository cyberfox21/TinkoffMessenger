package com.cyberfox21.tinkoffmessanger.data.api.response.dto

import com.google.gson.annotations.SerializedName

data class UserPresenceClientDTO(
    @SerializedName("status")
    val status: String
)