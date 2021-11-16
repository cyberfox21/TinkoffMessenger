package com.cyberfox21.tinkoffmessanger.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Channel(
    val id: Int,
    val name: String,
) : Parcelable
