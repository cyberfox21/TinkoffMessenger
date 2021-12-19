package com.cyberfox21.tinkoffmessanger.data.api.response.dto

import com.google.gson.annotations.SerializedName

data class ReactionDTO(

    @SerializedName("user_id")
    var userId: Int = UNDEFINED_ID,

    @SerializedName("emoji_name")
    val name: String,

    @SerializedName("emoji_code")
    val code: String,

    @SerializedName("reaction_type")
    val type: String,

    ) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
