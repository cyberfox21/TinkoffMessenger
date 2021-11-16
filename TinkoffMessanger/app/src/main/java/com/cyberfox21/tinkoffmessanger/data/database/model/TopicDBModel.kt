package com.cyberfox21.tinkoffmessanger.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicDBModel(
    @PrimaryKey(autoGenerate = false)
    val title: String,
    val messagesCount: Int,
    val channelId: Int
)