package com.cyberfox21.tinkoffmessanger.domain.entity

data class Reaction(
    val reaction: String,
    val name: String,
    val userId: Int,
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
