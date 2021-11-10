package com.cyberfox21.tinkoffmessanger.domain.entity

data class User(
    val id: Int,
    val avatar: String,
    val name: String,
    val email: String,
    val status: Boolean
)
