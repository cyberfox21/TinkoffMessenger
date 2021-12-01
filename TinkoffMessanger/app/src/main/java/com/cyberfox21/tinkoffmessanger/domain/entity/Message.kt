package com.cyberfox21.tinkoffmessanger.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Message(
    val id: Int,
    val message: String,
    val time: Date,
    val senderId: Int,
    val senderName: String,
    val senderAvatarUrl: String,
    val isCurrentUser: Boolean,
    val reactions: List<Reaction>
) : Parcelable
