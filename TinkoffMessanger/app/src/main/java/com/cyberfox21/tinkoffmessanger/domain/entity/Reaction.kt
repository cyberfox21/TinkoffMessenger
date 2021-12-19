package com.cyberfox21.tinkoffmessanger.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reaction(
    val userId: Int,
    val code: String,
    val name: String,
    val reaction: String,
    val type: String
) : Parcelable {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
