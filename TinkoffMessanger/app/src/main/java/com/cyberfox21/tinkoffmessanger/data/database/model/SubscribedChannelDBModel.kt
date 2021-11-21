package com.cyberfox21.tinkoffmessanger.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscribedChannels")
data class SubscribedChannelDBModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
)
