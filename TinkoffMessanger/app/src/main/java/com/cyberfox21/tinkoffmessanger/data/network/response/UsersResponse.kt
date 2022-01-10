package com.cyberfox21.tinkoffmessanger.data.network.response


import com.cyberfox21.tinkoffmessanger.data.network.response.dto.UserDTO
import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("members")
    val members: List<UserDTO>
)
