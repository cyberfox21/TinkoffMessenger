package com.cyberfox21.tinkoffmessanger.data.api.response.dto

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("user_id")
    val id: Int,

    @SerializedName("full_name")
    val full_name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("avatar_url")
    val avatar_url: String,

    @SerializedName("is_active")
    val is_active: Boolean
)
