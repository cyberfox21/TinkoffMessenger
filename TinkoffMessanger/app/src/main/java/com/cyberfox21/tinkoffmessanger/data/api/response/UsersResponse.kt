package com.cyberfox21.tinkoffmessanger.data.api.response


import com.cyberfox21.tinkoffmessanger.data.api.response.dto.UserDTO
import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("members")
    val members: List<UserDTO>
)
