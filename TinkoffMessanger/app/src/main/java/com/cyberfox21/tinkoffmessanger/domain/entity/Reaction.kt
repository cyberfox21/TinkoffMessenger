package com.cyberfox21.tinkoffmessanger.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reaction(
    val reaction: String,
    val name: String,
    val userId: Int,
) : Parcelable {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
