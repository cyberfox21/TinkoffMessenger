package com.cyberfox21.tinkoffmessanger.data.api.response.dto

import com.google.gson.annotations.SerializedName

data class ReactionDTO(
    @SerializedName("emoji_code")
    val code: String,

    @SerializedName("emoji_name")
    val name: String,

    @SerializedName("user_id")
    var userId: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
