package com.cyberfox21.tinkoffmessanger.data.api.response

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class ReactionsResponse(
    @SerializedName("name_to_codepoint")
    val reactionsObject: JsonObject
)
