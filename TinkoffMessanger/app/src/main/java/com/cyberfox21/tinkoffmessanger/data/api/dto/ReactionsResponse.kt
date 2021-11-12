package com.cyberfox21.tinkoffmessanger.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonObject

data class ReactionsResponse(
    @SerialName("name_to_codepoint")
    val reactionsObject: JsonObject
)