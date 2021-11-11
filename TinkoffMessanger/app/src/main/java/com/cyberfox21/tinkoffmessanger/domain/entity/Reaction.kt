package com.cyberfox21.tinkoffmessanger.domain.entity

data class Reaction(
    val userId: Int,
    val name: String,
    val reaction: String,
)
