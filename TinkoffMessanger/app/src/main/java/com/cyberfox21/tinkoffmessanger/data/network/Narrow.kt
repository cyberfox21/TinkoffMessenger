package com.cyberfox21.tinkoffmessanger.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Narrow(
    @SerialName("operator")
    val operator: String,

    @SerialName("operand")
    val operand: String
)
