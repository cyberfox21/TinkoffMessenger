package com.cyberfox21.tinkoffmessanger.data.repository

import com.cyberfox21.tinkoffmessanger.domain.repository.ReactionRepository

object ReactionRepositoryImpl : ReactionRepository {
    var reactions = mutableListOf<String>()

    init {
        for (i in 0 until 50) {
            val unicode = 0x1F600 + i
            reactions.add(String(Character.toChars(unicode)))
        }
    }

    override fun getReactionList(): List<String> = reactions.toList()
}
