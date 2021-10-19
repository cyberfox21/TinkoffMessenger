package com.cyberfox21.tinkoffmessanger.domain.entity

data class Message(
    val id: Int,
    val image: Int,
    val name: String,
    val msg: String,
    val time: String,
    val reactions: List<Reaction>
)