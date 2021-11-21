package com.cyberfox21.tinkoffmessanger.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reaction_list")
data class ReactionListDBModel(
    @PrimaryKey(autoGenerate = false)
    val reaction: String,
    val name: String
)