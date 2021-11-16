package com.cyberfox21.tinkoffmessanger.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction
import java.util.*

@Entity(tableName = "messages")
data class MessageDBModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val message: String,
    val time: Date,
    val senderId: Int,
    val senderName: String,
    val senderAvatarUrl: String,
    val isCurrentUser: Boolean,
    val reactions: List<Reaction>
)