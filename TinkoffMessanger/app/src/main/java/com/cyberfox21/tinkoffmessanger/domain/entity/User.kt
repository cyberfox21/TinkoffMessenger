package com.cyberfox21.tinkoffmessanger.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val avatar: String,
    val name: String,
    val email: String,
    val status: Boolean
) : Parcelable
