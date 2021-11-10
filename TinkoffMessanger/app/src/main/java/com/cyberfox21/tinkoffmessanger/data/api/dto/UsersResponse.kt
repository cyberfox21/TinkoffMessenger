package com.cyberfox21.tinkoffmessanger.data.api.dto


import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("members")
    val members: List<UserDTO>
)