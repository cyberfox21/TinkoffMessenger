package com.cyberfox21.tinkoffmessanger.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channels")
data class ChannelDBModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String
)