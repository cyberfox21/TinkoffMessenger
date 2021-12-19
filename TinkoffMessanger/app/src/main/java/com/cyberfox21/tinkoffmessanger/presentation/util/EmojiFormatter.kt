package com.cyberfox21.tinkoffmessanger.presentation.util

import com.cyberfox21.tinkoffmessanger.domain.entity.Reaction

object EmojiFormatter {

    @Throws(NumberFormatException::class)
    fun simpleToEmoji(code: String): String {
        return String(Character.toChars(Integer.parseInt(code, 16)))
    }

    private fun combinedToEmoji(code: String): String {
        val codes = code.split("-")
        return simpleToEmoji(codes[0]) + simpleToEmoji(codes[1])
    }

    private fun utfToEmoji(code: String): String {
        return if (code.contains("-")) {
            combinedToEmoji(code)
        } else {
            simpleToEmoji(code)
        }
    }

    fun codeToEmoji(code: String): String {
        return try {
            utfToEmoji(code)
        } catch (e: NumberFormatException) {
            UNKNOWN_EMOJI
        }
    }

    fun jsonToEmoji(code: String): String {
        return try {
            codeToEmoji(code.replace("\"", ""))
        } catch (e: NumberFormatException) {
            ""
        }
    }

    fun jsonObjectToReactionsList(jsonObject: com.google.gson.JsonObject): List<Reaction> {
        val reactions = mutableListOf<Reaction>()
        val emojis = mutableSetOf<String>()
        jsonObject.keySet().forEach { key ->
            val code = jsonObject[key].toString()
            val emoji = jsonToEmoji(code)
            if (emoji.isNotEmpty() && !emojis.contains(emoji)) {
                reactions.add(
                    Reaction(
                        userId = -1,
                        name = key,
                        code = code,
                        type = "",
                        reaction = emoji
                    )
                )
                emojis.add(emoji)
            }
        }
        return reactions
    }

    private const val UNKNOWN_EMOJI = "⬜"
}