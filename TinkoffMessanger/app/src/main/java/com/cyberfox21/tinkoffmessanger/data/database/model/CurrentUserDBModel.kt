package com.cyberfox21.tinkoffmessanger.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_user")
data class CurrentUserDBModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val avatar: String,
    val name: String,
    val email: String,
    val status: Boolean
)