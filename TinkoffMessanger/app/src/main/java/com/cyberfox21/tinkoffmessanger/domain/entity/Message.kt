package com.cyberfox21.tinkoffmessanger.domain.entity

import java.util.*

data class Message(
    val id: Int,
    val message: String,
    val time: Date,
    val senderId: Int,
    val senderName: String,
    val senderAvatarUrl: String,
    val isCurrentUser: Boolean,
    val reactions: List<Reaction>
)
